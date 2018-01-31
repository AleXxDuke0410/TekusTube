package co.tekus.tekustube.tekustube.util;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wrmej on 30/01/2018.
 */

public abstract class DoubleClickListener implements View.OnTouchListener {

    private static final long DOUBLE_CLICK_TIME_DELTA = 300;

    long lastClickTime = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        long clickTime = System.currentTimeMillis();

        if ((clickTime - lastClickTime) < DOUBLE_CLICK_TIME_DELTA) {
            onDoubleClick(v);
            lastClickTime = 0;
            lastClickTime = clickTime;
        } else {
            //onSingleClick(v);
        }

        lastClickTime = clickTime;

        return false;
    }

    //public abstract void onSingleClick(View v);
    public abstract void onDoubleClick(View v);
}