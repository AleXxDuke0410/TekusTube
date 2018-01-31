package co.tekus.tekustube.tekustube;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import co.tekus.tekustube.tekustube.adapters.VideoListAdapter;
import co.tekus.tekustube.tekustube.database.DatabaseHelper;
import co.tekus.tekustube.tekustube.database.querys.VideoQuery;
import co.tekus.tekustube.tekustube.download.DownloadVideo;
import co.tekus.tekustube.tekustube.util.DoubleClickListener;
import co.tekus.tekustube.tekustube.util.Utils;
import co.tekus.tekustube.tekustube.video.FullScreenVideoActivity;

public class MainActivity extends AppCompatActivity {
    private TextView tvInformationTest;
    private ListView videoList;
    private RelativeLayout rlNoVideos;
    private VideoListAdapter adapter;
    private VideoQuery videoQuery = new VideoQuery();
    boolean doubleBackToExitPressedOnce = false;

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
                Intent i = new Intent(MainActivity.this, FullScreenVideoActivity.class);
                i.putExtra("fullScreenInd", false);
                startActivity(i);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadVideo downloadVideo = new DownloadVideo(MainActivity.this);
                downloadVideo.execute(Utils.VIDEO_URL, "MiVideoTekus.mp4");/**/
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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
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
        else if (rlNoVideos.getVisibility() != View.VISIBLE)
            rlNoVideos.setVisibility(View.VISIBLE);
    }
}
