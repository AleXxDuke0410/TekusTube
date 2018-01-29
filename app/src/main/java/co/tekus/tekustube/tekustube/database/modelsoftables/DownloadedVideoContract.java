package co.tekus.tekustube.tekustube.database.modelsoftables;

import android.provider.BaseColumns;

/**
 * Created by wrmej on 27/01/2018.
 */

public class DownloadedVideoContract {

    private DownloadedVideoContract() {}

    public static class DownloadedVideo implements BaseColumns {
        public static final String TABLE_NAME = "downloadedVideo";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DIR = "localDir";
        public static final String COLUMN_DURATION = "duration";

        public static final String TABLE_NOTIFICATION_NAME = "notication";
        public static final String COLUMN_ID = "NotificationId";
        public static final String COLUMN_DATE = "Date";
        public static final String COLUMN_DURA = "Duration";
    }
}
