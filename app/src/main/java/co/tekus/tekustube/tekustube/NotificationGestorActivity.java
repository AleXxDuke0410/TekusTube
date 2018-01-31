package co.tekus.tekustube.tekustube;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import co.tekus.tekustube.tekustube.adapters.NotificationListAdapter;
import co.tekus.tekustube.tekustube.database.DatabaseHelper;
import co.tekus.tekustube.tekustube.database.querys.NotificationQuery;
import co.tekus.tekustube.tekustube.http.Notifications;
import co.tekus.tekustube.tekustube.models.Notification;
import co.tekus.tekustube.tekustube.tekusdialogs.TekusDialogOnClick;
import co.tekus.tekustube.tekustube.tekusdialogs.TekusInfoDialog;
import co.tekus.tekustube.tekustube.util.Utils;

public class NotificationGestorActivity extends AppCompatActivity {
    private ListView notificationList;
    private NotificationListAdapter adapter;
    private NotificationQuery notificationQuery;
    private RelativeLayout rlNoNotifications;
    private LinearLayout llNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_gestor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initControls();

        adapter = new NotificationListAdapter(this, notificationQuery.getNotifications(
                new DatabaseHelper(this).getWritableDatabase()));

        notificationList.setAdapter(adapter);
        if (adapter.getCount() > 0)
            rlNoNotifications.setVisibility(View.GONE);
        else
            llNotifications.setVisibility(View.GONE);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNotificationOfServer(true);
            }
        });

        getNotificationOfServer(false);
    }

    private void getNotificationOfServer(boolean showDialog) {
        URL url = null;
        try {
            url = new URL("http://proyectos.tekus.co/Test/api/notifications/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (url != null) {
            Notifications notifications = new Notifications(NotificationGestorActivity.this,
                    Utils.METHOD_GET, showDialog);
            notifications.execute(url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_notification_delete) {
            TekusInfoDialog infoDialog = new TekusInfoDialog(this);
            infoDialog.setTitle(getString(R.string.app_name));
            infoDialog.setMessage(getString(R.string.confir_delete_notifications));
            infoDialog.setPositiveButton(getString(R.string.txt_yes), new TekusDialogOnClick() {
                @Override
                public void onClick(View view) {
                    JSONObject notiIdArray = new JSONObject();
                    JSONArray array = getNotificationsId(adapter);
                    if (array.length() > 0) {
                        try {
                            notiIdArray.put("arrayId", array);

                            URL url = null;
                            try {
                                url = new URL(Utils.API_URL);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }

                            if (url != null) {
                                Notifications notifications = new Notifications(NotificationGestorActivity.this,
                                        Utils.METHOD_DELETE, notiIdArray);
                                notifications.execute(url);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        TekusInfoDialog tekusInfoDialog = new TekusInfoDialog(NotificationGestorActivity.this);
                        tekusInfoDialog.setTitle(getString(R.string.app_name));
                        tekusInfoDialog.setMessage(getString(R.string.txt_all_notifications_deleted));
                        tekusInfoDialog.setPositiveButton(getString(R.string.txt_ok), null);
                        tekusInfoDialog.show();
                    }
                }
            });
            infoDialog.setNegativeButton(getString(R.string.txt_no), null);
            infoDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initControls() {
        rlNoNotifications = findViewById(R.id.rlNoNotification);
        llNotifications = findViewById(R.id.llNotification);
        notificationList = findViewById(R.id.notificationList);
        notificationQuery = new NotificationQuery();
    }

    public void refreshLayout() {
        adapter = new NotificationListAdapter(this, notificationQuery.getNotifications(
                new DatabaseHelper(this).getWritableDatabase()));

        notificationList.setAdapter(adapter);
        if (adapter.getCount() > 0) {
            rlNoNotifications.setVisibility(View.GONE);
            if (llNotifications.getVisibility() != View.VISIBLE)
                llNotifications.setVisibility(View.VISIBLE);
        } else {
            llNotifications.setVisibility(View.GONE);
            if (rlNoNotifications.getVisibility() != View.VISIBLE)
                rlNoNotifications.setVisibility(View.VISIBLE);
        }
    }

    public JSONArray getNotificationsId(NotificationListAdapter adapter) {
        JSONArray arrayIds = new JSONArray();

        for (int i = 0; i < adapter.getCount(); i++) {
            Notification item = adapter.getItem(i);
            arrayIds.put(item.getNotificationId());
        }

        return arrayIds;
    }
}
