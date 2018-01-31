package co.tekus.tekustube.tekustube.download;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import co.tekus.tekustube.tekustube.MainActivity;
import co.tekus.tekustube.tekustube.R;
import co.tekus.tekustube.tekustube.database.DatabaseHelper;
import co.tekus.tekustube.tekustube.database.modelsoftables.DownloadedVideoContract.DownloadedVideo;
import co.tekus.tekustube.tekustube.database.querys.VideoQuery;
import co.tekus.tekustube.tekustube.http.Notifications;
import co.tekus.tekustube.tekustube.models.Video;
import co.tekus.tekustube.tekustube.tekusdialogs.TekusDialogOnClick;
import co.tekus.tekustube.tekustube.tekusdialogs.TekusDownloadDialog;
import co.tekus.tekustube.tekustube.tekusdialogs.TekusInfoDialog;
import co.tekus.tekustube.tekustube.util.Utils;

/**
 * Created by wrmej on 27/01/2018.
 */

public class DownloadVideo extends AsyncTask<String, Long, Video> {
    private Context context;
    private TekusDownloadDialog alert;
    private URL url;
    private HttpsURLConnection urlConnection;
    final Timer myTimer = new Timer();
    private final int[] s = {0};

    // TODO: CONSTRUCTOR
    public DownloadVideo(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        updateAndroidSecurityProvider((Activity) context);

        alert = new TekusDownloadDialog(context);
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        alert.setNeutralButton(context.getString(R.string.txt_cancel), new TekusDialogOnClick() {
            @Override
            public void onClick(View view) {
                TekusInfoDialog cancelQuetion = new TekusInfoDialog(context);
                cancelQuetion.setTitle(context.getString(R.string.app_name));
                cancelQuetion.setCancelable(false);
                cancelQuetion.setCanceledOnTouchOutside(false);
                cancelQuetion.setMessage(context.getString(R.string.cancel_download_video));
                cancelQuetion.setPositiveButton(context.getString(R.string.txt_yes), new TekusDialogOnClick() {
                    @Override
                    public void onClick(View view) {
                        cancel(true);
                    }
                });
                cancelQuetion.setNegativeButton(context.getString(R.string.txt_no), new TekusDialogOnClick() {
                    @Override
                    public void onClick(View view) {
                        alert.show();
                    }
                });
                cancelQuetion.show();
            }
        });
        alert.show();

        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                s[0]++;
            }
        }, 0, 1000);
    }

    @Override
    protected Video doInBackground(String... strings) {
        alert.setTitle(strings[1]);

        try {
            // Origen del archivo. Ruta completa.
            url = new URL(strings[0]);

            // Establecer conección
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod(Utils.METHOD_GET);
            urlConnection.connect();

            // Ruta de destino en el dispositivo
            File SDCardRoot = new File(Environment.getExternalStorageDirectory() + File.separator +
                    context.getString(R.string.app_name) + File.separator + Utils.FOLDER_ROUTE);

            boolean success = true;
            if (!SDCardRoot.exists())       // Se crea la ruta local de almacenamiento si no existe
                success = SDCardRoot.mkdirs();

            if (success) {
                // Archivo que contendrá el contenido descargado
                File file = new File(SDCardRoot, strings[1]);

                // Objeto que permite escribir el archivo descargado en el dispositivo
                FileOutputStream fileOutput = new FileOutputStream(file);

                // Leer datos que devuelve la URL
                InputStream inputStream = urlConnection.getInputStream();

                // Tamaño del archivo que se desea descargar
                long totalSize = urlConnection.getContentLength();

                // Variable utilizada pra guardar el progreso de descarga
                long downloadedSize = 0;

                // Utilizar una variable tipo buffer y una variable para ir almacenar el tamaño temporal
                byte[] buffer = new byte[1024];
                int bufferLength = 0;

                // Se comienza a recorrer el buffer y se aumenta el valor total descargado
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;

                    publishProgress(downloadedSize, totalSize);
                }

                // Cerrar proceso de escritura del archivo
                fileOutput.close();

                return new Video(file.getName(), file.getAbsolutePath(), Utils.TEMP_DURATION_VIDEO);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(final Long... values) {
        alert.setPorcentaje(((values[0] * 100) / values[1]));
    }

    @Override
    protected void onCancelled() {
        Toast.makeText(context, context.getString(R.string.canceled_download), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(Video video) {
        myTimer.cancel();
        alert.dismiss();

        if (video != null) {
            new VideoQuery().insert(new DatabaseHelper(context).getWritableDatabase(), video);

            JSONObject jsonItem = null;
            try {
                String formattedDate = new SimpleDateFormat(Utils.SIMPLE_FORMAT)
                        .format(Calendar.getInstance().getTime());

                jsonItem = new JSONObject();
                jsonItem.put(DownloadedVideo.COLUMN_ID, 4444);
                jsonItem.put(DownloadedVideo.COLUMN_DATE, formattedDate);
                jsonItem.put(DownloadedVideo.COLUMN_DURA, s[0]);

                URL urlNotification = null;
                try {
                    urlNotification = new URL(Utils.API_URL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                if (urlNotification != null) {
                    Notifications notifications = new Notifications(context, Utils.METHOD_POST, jsonItem);
                    notifications.execute(urlNotification);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ((MainActivity) context).refreshLayout();
        }
    }

    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(callingActivity);
        } catch (GooglePlayServicesRepairableException e) {
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
}