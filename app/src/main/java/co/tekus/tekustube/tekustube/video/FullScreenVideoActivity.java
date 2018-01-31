package co.tekus.tekustube.tekustube.video;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import co.tekus.tekustube.tekustube.NotificationGestorActivity;
import co.tekus.tekustube.tekustube.R;
import co.tekus.tekustube.tekustube.database.DatabaseHelper;
import co.tekus.tekustube.tekustube.database.modelsoftables.DownloadedVideoContract.DownloadedVideo;
import co.tekus.tekustube.tekustube.util.DoubleClickListener;

/**
 * Created by wrmej on 28/01/2018.
 */

public class FullScreenVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_videoview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        videoView = findViewById(R.id.videoView);

        boolean fullScreen = getIntent().getBooleanExtra("fullScreenInd", false);
        if (fullScreen) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        int currrentTime = getIntent().getIntExtra("currentTime", 0);

        Cursor cursor = new DatabaseHelper(this).getWritableDatabase().rawQuery(
                "SELECT * FROM " + DownloadedVideo.TABLE_NAME + ";", null);
        String videoPath = "";
        if (cursor.moveToNext())
            videoPath = cursor.getString(cursor.getColumnIndex(DownloadedVideo.COLUMN_DIR));

        videoView.setVideoPath(videoPath);

        // Detectar doble click y abrir el gestor de notificaciones
        videoView.setOnTouchListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                startActivity(new Intent(FullScreenVideoActivity.this, NotificationGestorActivity.class));
            }
        });

        mediaController = new FullScreenMediaController(this, videoView);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.seekTo(currrentTime);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        videoView.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    private boolean isLandScape() {
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();

        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)
            return true;

        return false;
    }
}