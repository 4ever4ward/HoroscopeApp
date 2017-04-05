package ua.matvienko_apps.horoscope.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alexandr on 02/04/2017.
 */

public class AppDBHelper extends SQLiteOpenHelper {

    public AppDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createForecastTableQuery = "CREATE TABLE "
                + AppDBContract.ForecastEntries.TABLE_NAME + " ("
                + AppDBContract.ForecastEntries.COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, "
                + AppDBContract.ForecastEntries.COLUMN_TEXT + " TEXT NOT NULL, "
                + AppDBContract.ForecastEntries.COLUMN_BUSINESS + " INTEGER NOT NULL, "
                + AppDBContract.ForecastEntries.COLUMN_LOVE + " INTEGER NOT NULL, "
                + AppDBContract.ForecastEntries.COLUMN_HEALTH + " INTEGER NOT NULL, "
                + AppDBContract.ForecastEntries.COLUMN_PERIOD + " TEXT NOT NULL, "
                + AppDBContract.ForecastEntries.COLUMN_SIGN + " TEXT NOT NULL, "
                + AppDBContract.ForecastEntries.COLUMN_DATE + " TEXT NOT NULL " + " );";

        db.execSQL(createForecastTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + AppDBContract.ForecastEntries.TABLE_NAME);
        }
    }
}
