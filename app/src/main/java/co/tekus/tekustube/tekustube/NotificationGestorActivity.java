package co.tekus.tekustube.tekustube;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.net.MalformedURLException;
import java.net.URL;

import co.tekus.tekustube.tekustube.adapters.NotificationListAdapter;
import co.tekus.tekustube.tekustube.adapters.VideoListAdapter;
import co.tekus.tekustube.tekustube.database.DatabaseHelper;
import co.tekus.tekustube.tekustube.database.querys.NotificationQuery;
import co.tekus.tekustube.tekustube.database.querys.VideoQuery;
import co.tekus.tekustube.tekustube.http.Notifications;
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
                URL url = null;
                try {
                    url = new URL("http://proyectos.tekus.co/Test/api/notifications/");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                if (url != null) {
                    Notifications notifications = new Notifications(NotificationGestorActivity.this, Utils.METHOD_GET);
                    notifications.execute(url);
                }
            }
        });
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
            infoDialog.setTitle(getString(R.string.action_delete_notifications));
            infoDialog.setMessage(getString(R.string.txt_confir_delete_notifications));
            infoDialog.setPositiveButton(getString(R.string.txt_si), new TekusDialogOnClick() {
                @Override
                public void onClick(View view) {

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
        if (adapter.getCount() > 0)
            rlNoNotifications.setVisibility(View.GONE);
        else
            llNotifications.setVisibility(View.GONE);
    }
}
