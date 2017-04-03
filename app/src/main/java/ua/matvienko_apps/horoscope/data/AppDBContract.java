package ua.matvienko_apps.horoscope.data;

import android.provider.BaseColumns;

/**
 * Created by Alexandr on 02/04/2017.
 */

public class AppDBContract {

    public static final String DB_NAME = "horoscope.db";
    public static final int DB_VERSION = 1;

    public static final class ForecastEntries implements BaseColumns{
        public static final String TABLE_NAME = "forecasts";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_BUSINESS = "business";
        public static final String COLUMN_LOVE = "love";
        public static final String COLUMN_HEALTH = "health";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_SIGN = "sign";
    }

}
