package co.tekus.tekustube.tekustube.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.tekus.tekustube.tekustube.database.modelsoftables.DownloadedVideoContract.DownloadedVideo;

/**
 * Created by wrmej on 27/01/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TekusTube.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_VIDEO = "CREATE TABLE " + DownloadedVideo.TABLE_NAME + " (" +
            DownloadedVideo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DownloadedVideo.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
            DownloadedVideo.COLUMN_DIR + TEXT_TYPE + COMMA_SEP +
            DownloadedVideo.COLUMN_DURATION + TEXT_TYPE + ")";

    private static final String SQL_CREATE_NOTIFICATION = "CREATE TABLE " + DownloadedVideo.TABLE_NOTIFICATION_NAME + " (" +
            DownloadedVideo.COLUMN_ID + " INTEGER PRIMARY KEY," +
            DownloadedVideo.COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
            DownloadedVideo.COLUMN_DURA + INTEGER_TYPE + ")";

    private static final String SQL_DELETE_VIDEO = "DROP TABLE IF EXISTS " + DownloadedVideo.TABLE_NAME;
    private static final String SQL_DELETE_NOTIF = "DROP TABLE IF EXISTS " + DownloadedVideo.TABLE_NOTIFICATION_NAME;

    // TODO: COSNTRUCTOR
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_VIDEO);
        db.execSQL(SQL_CREATE_NOTIFICATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_VIDEO);
        db.execSQL(SQL_DELETE_NOTIF);

        onCreate(db);
    }
}
