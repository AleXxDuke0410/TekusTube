package co.tekus.tekustube.tekustube.database.querys;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import co.tekus.tekustube.tekustube.database.DatabaseHelper;
import co.tekus.tekustube.tekustube.database.modelsoftables.DownloadedVideoContract.DownloadedVideo;
import co.tekus.tekustube.tekustube.models.Video;

/**
 * Created by wrmej on 27/01/2018.
 */

public class VideoQuery {

    public void insert(SQLiteDatabase db, Video video){
        ContentValues values = new ContentValues();
        values.put(DownloadedVideo.COLUMN_NAME, video.getNameVideo());
        values.put(DownloadedVideo.COLUMN_DIR, video.getDir());
        values.put(DownloadedVideo.COLUMN_DURATION, video.getDuration());

        db.insert(DownloadedVideo.TABLE_NAME, null, values);
        db.close();
    }

    public List<Video> getVideos(SQLiteDatabase db){
        List<Video> videoList = new ArrayList<Video>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DownloadedVideo.TABLE_NAME + ";", null);
        while (cursor.moveToNext()){
            String nameVideo = cursor.getString(cursor.getColumnIndex(DownloadedVideo.COLUMN_NAME));
            String dirVideo = cursor.getString(cursor.getColumnIndex(DownloadedVideo.COLUMN_DIR));
            String durationVideo = cursor.getString(cursor.getColumnIndex(DownloadedVideo.COLUMN_DURATION));

            videoList.add(new Video(nameVideo, dirVideo, durationVideo));
        }
        cursor.close();
        db.close();

        return videoList;
    }
}
