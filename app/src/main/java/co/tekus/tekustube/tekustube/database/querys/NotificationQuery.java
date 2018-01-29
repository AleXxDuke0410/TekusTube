package co.tekus.tekustube.tekustube.database.querys;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import co.tekus.tekustube.tekustube.database.modelsoftables.DownloadedVideoContract.DownloadedVideo;
import co.tekus.tekustube.tekustube.models.Notification;
import co.tekus.tekustube.tekustube.models.Video;

/**
 * Created by wrmej on 27/01/2018.
 */

public class NotificationQuery {

    public void insert(SQLiteDatabase db, Notification notification){
        ContentValues values = new ContentValues();
        values.put(DownloadedVideo.COLUMN_ID, notification.getNotificationId());
        values.put(DownloadedVideo.COLUMN_DATE, notification.getDate());
        values.put(DownloadedVideo.COLUMN_DURA, notification.getDuration());

        db.insert(DownloadedVideo.TABLE_NOTIFICATION_NAME, null, values);
    }

    public List<Notification> getNotifications(SQLiteDatabase db){
        List<Notification> notificationList = new ArrayList<Notification>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DownloadedVideo.TABLE_NOTIFICATION_NAME + ";", null);
        while (cursor.moveToNext()){
            int notificationId = cursor.getInt(cursor.getColumnIndex(DownloadedVideo.COLUMN_ID));
            String notificationDate = cursor.getString(cursor.getColumnIndex(DownloadedVideo.COLUMN_DATE));
            int notificationDuration = cursor.getInt(cursor.getColumnIndex(DownloadedVideo.COLUMN_DURA));

            notificationList.add(new Notification(notificationId, notificationDate, notificationDuration));
        }
        cursor.close();
        db.close();

        return notificationList;
    }

    public void deleteAll(SQLiteDatabase db){
        db.execSQL("DELETE FROM " + DownloadedVideo.TABLE_NOTIFICATION_NAME);
    }

    public int getCount(SQLiteDatabase db){
        Cursor f = db.rawQuery("SELECT * FROM " + DownloadedVideo.TABLE_NOTIFICATION_NAME, null);
        int count = f.getCount();
        f.close();

        return count;
    }
}
