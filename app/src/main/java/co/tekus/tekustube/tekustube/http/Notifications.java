package co.tekus.tekustube.tekustube.http;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import co.tekus.tekustube.tekustube.NotificationGestorActivity;
import co.tekus.tekustube.tekustube.R;
import co.tekus.tekustube.tekustube.database.DatabaseHelper;
import co.tekus.tekustube.tekustube.database.modelsoftables.DownloadedVideoContract;
import co.tekus.tekustube.tekustube.database.querys.NotificationQuery;
import co.tekus.tekustube.tekustube.models.Notification;
import co.tekus.tekustube.tekustube.tekusdialogs.TekusDialogOnClick;
import co.tekus.tekustube.tekustube.tekusdialogs.TekusInfoDialog;
import co.tekus.tekustube.tekustube.util.Utils;

/**
 * Created by wrmej on 28/01/2018.
 */

public class Notifications extends AsyncTask<URL, Void, String> {
    private Context context;
    private HttpURLConnection urlConnection;
    private static final String CONTENT_TYPE = "application/json";
    private static final String IDENTIFICATOR_AUTH = "Basic ";
    private String USER_IDENTIFICATION = "900590282";
    private String DATE;
    private SQLiteDatabase db;
    private NotificationQuery notificationQuery;
    private TekusInfoDialog infoDialog;
    private String method;
    private JSONObject objectToSend;
    private boolean showDialogInGETMethod;

    public Notifications(Context context, String method, boolean showDialog) {
        this.context = context;
        this.method = method;
        showDialogInGETMethod = showDialog;
    }

    public Notifications(Context context, String method, JSONObject objectToSend) {
        this.context = context;
        this.method = method;
        this.objectToSend = objectToSend;
    }

    @Override
    protected void onPreExecute() {
        DATE = new SimpleDateFormat(Utils.SIMPLE_FORMAT).format(Calendar.getInstance().getTime());

        updateAndroidSecurityProvider((Activity) context);

        db = new DatabaseHelper(context).getWritableDatabase();
        notificationQuery = new NotificationQuery();

        infoDialog = new TekusInfoDialog(context);

        if (method.equals(Utils.METHOD_GET))
            infoDialog.setTitle(context.getString(R.string.consulting_notifications));
        else if (method.equals(Utils.METHOD_POST))
            infoDialog.setTitle(context.getString(R.string.sending_notification));
        else if (method.equals(Utils.METHOD_PUT))
            infoDialog.setTitle(context.getString(R.string.updating_notification));
        else if (method.equals(Utils.METHOD_DELETE))
            infoDialog.setTitle(context.getString(R.string.eliminating_notifications));

        infoDialog.setMessage(context.getString(R.string.please_wait));
        infoDialog.setCancelable(false);
        infoDialog.setCanceledOnTouchOutside(false);
        infoDialog.showProgressBar(true);

        if (method.equals(Utils.METHOD_GET)) {
            if (showDialogInGETMethod)
                infoDialog.show();
        } else
            infoDialog.show();
    }

    @Override
    protected String doInBackground(URL... urls) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = urls[0];
            urlConnection = (HttpURLConnection) url.openConnection(/*proxy*/);
            if (method.equals(Utils.METHOD_POST))
                urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", CONTENT_TYPE);
            urlConnection.setRequestProperty("Authorization", IDENTIFICATOR_AUTH + USER_IDENTIFICATION);
            urlConnection.setRequestProperty("Date", DATE);
            urlConnection.setConnectTimeout(20 * 1000);
            urlConnection.setReadTimeout(20 * 1000);

            switch (method) {
                case Utils.METHOD_GET:
                    urlConnection.setRequestMethod(Utils.METHOD_GET);

                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        String line;
                        while ((line = reader.readLine()) != null)
                            result.append(line);
                    }
                    break;
                case Utils.METHOD_POST:
                    urlConnection.setRequestMethod(Utils.METHOD_POST);

                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(objectToSend.toString());

                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = urlConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        String line;
                        while ((line = reader.readLine()) != null)
                            result.append(line);
                    }
                    break;
                case Utils.METHOD_PUT:

                    break;
                case Utils.METHOD_DELETE:
                    try {
                        NotificationQuery notificationQuery = new NotificationQuery();
                        JSONArray arrayId = objectToSend.getJSONArray("arrayId");
                        for (int i = 0; i < arrayId.length(); i++) {
                            URL deleteUrl = new URL(urls[0].toString() + arrayId.get(i));
                            urlConnection = (HttpURLConnection) deleteUrl.openConnection(/*proxy*/);
                            urlConnection.setRequestMethod(Utils.METHOD_DELETE);
                            urlConnection.setDoOutput(true);
                            urlConnection.setDoInput(true);
                            urlConnection.setRequestProperty("Content-Type", CONTENT_TYPE);
                            urlConnection.setRequestProperty("Authorization", IDENTIFICATOR_AUTH + USER_IDENTIFICATION);
                            urlConnection.setRequestProperty("Date", DATE);
                            urlConnection.setConnectTimeout(20 * 1000);
                            urlConnection.setReadTimeout(20 * 1000);

                            int resCode = urlConnection.getResponseCode();
                            if (resCode == HttpURLConnection.HTTP_OK || resCode == HttpURLConnection.HTTP_NO_CONTENT)
                                notificationQuery.delete(db, (Integer) arrayId.get(i));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        switch (method) {
            case Utils.METHOD_GET:
                try {
                    if (s != null && s.equals(""))
                        s = new JSONArray().toString();
                    saveLocally(db, notificationQuery, new JSONArray(s));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ((NotificationGestorActivity) context).refreshLayout();

                if (showDialogInGETMethod) {
                    infoDialog.setTitle(context.getString(R.string.app_name));
                    infoDialog.setMessage(context.getString(R.string.notification_updated));
                    infoDialog.showProgressBar(false);
                    infoDialog.setPositiveButton(context.getString(R.string.txt_ok), new TekusDialogOnClick() {
                        @Override
                        public void onClick(View view) {
                            db.close();
                        }
                    });
                } /*else
                    Toast.makeText(context, context.getString(R.string.data_update), Toast.LENGTH_SHORT).show();/**/
                break;
            case Utils.METHOD_POST:
                infoDialog.dismiss();
                break;
            case Utils.METHOD_PUT:
                infoDialog.dismiss();
                break;
            case Utils.METHOD_DELETE:
                infoDialog.dismiss();
                ((NotificationGestorActivity) context).refreshLayout();
                break;
        }
    }

    public void saveLocally(SQLiteDatabase db, NotificationQuery notificationQuery,
                            JSONArray notificationArray) throws JSONException {

        // ELIMINAR TODAS LAS NOTIFICACIONES ALMACENADAS
        notificationQuery.deleteAll(db);

        // RECORRER UNA A UNA LAS NOTIFICACIONES DEL ARRAY DE NOTIFICACIONES
        for (int i = 0; i < notificationArray.length(); i++) {
            JSONObject itemNotification = notificationArray.getJSONObject(i);

            Notification notification = new Notification(
                    itemNotification.getInt(DownloadedVideoContract.DownloadedVideo.COLUMN_ID),
                    itemNotification.getString(DownloadedVideoContract.DownloadedVideo.COLUMN_DATE),
                    itemNotification.getInt(DownloadedVideoContract.DownloadedVideo.COLUMN_DURA));

            notificationQuery.insert(db, notification);
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
