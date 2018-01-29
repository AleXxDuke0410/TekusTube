package co.tekus.tekustube.tekustube.tekusdialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import co.tekus.tekustube.tekustube.R;

/**
 * Created by wrmej on 27/01/2018.
 */

public class TekusDownloadDialog extends Dialog {
    private View bodyGPSView;
    private Dialog dialog;
    private TextView title, tekusPorcentaje;
    private AppCompatButton neutralButton;

    public TekusDownloadDialog(Context context) {
        super(context);
        bodyGPSView = View.inflate(context, R.layout.item_video_download_dialog, null);
        dialog = new AlertDialog.Builder(context).setView(bodyGPSView).create();

        title = bodyGPSView.findViewById(R.id.tekusTitleDownload);
        tekusPorcentaje = bodyGPSView.findViewById(R.id.tekusPorcentajeDownload);

        neutralButton = bodyGPSView.findViewById(R.id.tekusNeutralButtonDownload);
    }

    @Override
    public void show() {
        dialog.show();
    }

    @Override
    public void dismiss() {
        dialog.dismiss();
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
    }

    public void setPorcentaje(long porcentaje){
        tekusPorcentaje.setText(String.valueOf(porcentaje));
    }
}
