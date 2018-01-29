package co.tekus.tekustube.tekustube.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import co.tekus.tekustube.tekustube.R;

/**
 * Created by wrmej on 28/01/2018.
 */

public class FullScreenMediaController extends MediaController {
    private ImageView fullScreen;
    private boolean isFullScreen;
    private VideoView videoView;

    public FullScreenMediaController(Context context, VideoView videoView) {
        super(context);

        this.videoView = videoView;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        fullScreen = new ImageView(super.getContext());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        params.rightMargin = 80;

        addView(fullScreen, params);

        //fullscreen indicator from intent
        isFullScreen = ((Activity) getContext()).getIntent().getBooleanExtra("fullScreenInd", false);

        if (isFullScreen)
            fullScreen.setImageResource(R.drawable.ic_fullscreen_exit_white_24dp);
        else
            fullScreen.setImageResource(R.drawable.ic_fullscreen_white_24dp);

        //add listener to image button to handle full screen and exit full screen events
        fullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), FullScreenVideoActivity.class);

                if (isFullScreen)
                    intent.putExtra("fullScreenInd", false);
                else
                    intent.putExtra("fullScreenInd", true);

                intent.putExtra("currentTime", videoView.getCurrentPosition());

                ((Activity) getContext()).startActivity(intent);
            }
        });
    }
}