package co.tekus.tekustube.tekustube;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import co.tekus.tekustube.tekustube.adapters.VideoListAdapter;
import co.tekus.tekustube.tekustube.database.DatabaseHelper;
import co.tekus.tekustube.tekustube.database.modelsoftables.DownloadedVideoContract;
import co.tekus.tekustube.tekustube.database.querys.NotificationQuery;
import co.tekus.tekustube.tekustube.database.querys.VideoQuery;
import co.tekus.tekustube.tekustube.download.DownloadVideo;
import co.tekus.tekustube.tekustube.http.Notifications;
import co.tekus.tekustube.tekustube.models.Video;
import co.tekus.tekustube.tekustube.tekusdialogs.TekusInfoDialog;
import co.tekus.tekustube.tekustube.util.Utils;
import co.tekus.tekustube.tekustube.video.FullScreenVideoActivity;

public class MainActivity extends AppCompatActivity {
    private VideoView videoView;
    private TextView tvInformationTest;
    private ListView videoList;
    private RelativeLayout rlNoVideos;
    private VideoListAdapter adapter;
    private VideoQuery videoQuery = new VideoQuery();

    private String videoURL1 = "https://s3.amazonaws.com/tekus/media/Arkbox.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViewControls();

        adapter = new VideoListAdapter(this, videoQuery.getVideos(
                new DatabaseHelper(this).getWritableDatabase()));

        videoList.setAdapter(adapter);
        if (adapter.getCount() > 0)
            rlNoVideos.setVisibility(View.GONE);

        videoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                String videoSelectedPath = ((Video) a.getItemAtPosition(position)).getDir();

                //Intent i = new Intent(MainActivity.this, VideoActivity.class);
                //i.putExtra("videoPath", videoSelectedPath);
                Intent i = new Intent(MainActivity.this, FullScreenVideoActivity.class);
                i.putExtra("fullScreenInd", false);
                startActivity(i);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*DownloadVideo downloadVideo = new DownloadVideo(MainActivity.this);
                downloadVideo.execute(videoURL1, "MiVideoTekus.mp4");/**/

                URL url = null;
                try {
                    url = new URL("http://proyectos.tekus.co/Test/api/notifications/");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                if (url != null) {
                    Notifications notifications = new Notifications(MainActivity.this, Utils.METHOD_DELETE);
                    notifications.execute(url);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_notifications) {
            startActivity(new Intent(this, NotificationGestorActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViewControls() {
        tvInformationTest = findViewById(R.id.tvInformationTest);
        videoList = findViewById(R.id.videoList);
        rlNoVideos = findViewById(R.id.rlNoVideos);

        tvInformationTest.setText(Html.fromHtml(getString(R.string.txt_description_about_test)));
    }

    public void refreshLayout() {
        adapter = new VideoListAdapter(this, videoQuery.getVideos(
                new DatabaseHelper(this).getWritableDatabase()));

        videoList.setAdapter(adapter);
        if (adapter.getCount() > 0)
            rlNoVideos.setVisibility(View.GONE);
    }
}
