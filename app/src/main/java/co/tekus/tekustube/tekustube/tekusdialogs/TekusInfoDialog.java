package co.tekus.tekustube.tekustube.tekusdialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import co.tekus.tekustube.tekustube.R;

/**
 * Created by wrmej on 27/01/2018.
 */

public class TekusInfoDialog extends Dialog {
    private View bodyGPSView;
    private Dialog dialog;

    private TextView title;
    private TextView message;
    private AppCompatButton positiveButton;
    private AppCompatButton negativeButton;
    private AppCompatButton neutralButton;
    private ProgressBar progress;

    public TekusInfoDialog(Context context) {
        super(context);
        bodyGPSView = View.inflate(context, R.layout.item_video_info_dialog, null);
        dialog = new AlertDialog.Builder(context).setView(bodyGPSView).create();

        title = bodyGPSView.findViewById(R.id.tekusTitle);
        message = bodyGPSView.findViewById(R.id.tekusMessage);
        positiveButton = bodyGPSView.findViewById(R.id.tekusPositiveButton);
        negativeButton = bodyGPSView.findViewById(R.id.tekusNegativeButton);
        neutralButton = bodyGPSView.findViewById(R.id.tekusNeutralButton);
        progress = bodyGPSView.findViewById(R.id.indeterminateProgress);

        title.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
        positiveButton.setVisibility(View.GONE);
        negativeButton.setVisibility(View.GONE);
        neutralButton.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void show() {
        dialog.show();
    }

    @Override
    public void dismiss() {
        dialog.dismiss();
    }

    @Override
    public void setCancelable(boolean flag) {
        dialog.setCancelable(flag);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        this.title.setText(title);
        this.title.setVisibility(View.VISIBLE);
    }

    public void setMessage(String message) {
        this.message.setText(message);
        this.message.setVisibility(View.VISIBLE);
    }

    public void setPositiveButton(String btnText, final TekusDialogOnClick callBack) {
        positiveButton.setText(btnText);
        positiveButton.setVisibility(View.VISIBLE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null)
                    callBack.onClick(v);

                dialog.dismiss();
            }
        });
    }

    public void setNegativeButton(String btnText, final TekusDialogOnClick callBack) {
        negativeButton.setText(btnText);
        negativeButton.setVisibility(View.VISIBLE);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null)
                    callBack.onClick(v);

                dialog.dismiss();
            }
        });
    }

    public void setNeutralButton(String btnText, final TekusDialogOnClick callBack) {
        neutralButton.setText(btnText);
        neutralButton.setVisibility(View.VISIBLE);
        neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null)
                    callBack.onClick(v);

                dialog.dismiss();
            }
        });
    }

    public void showProgressBar(Boolean showProgressBar) {
        progress.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
    }
}
