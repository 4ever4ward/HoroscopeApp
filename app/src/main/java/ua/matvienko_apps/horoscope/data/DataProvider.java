package ua.matvienko_apps.horoscope.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;

import ua.matvienko_apps.horoscope.Forecast;

/**
 * Created by alex_ on 04-Apr-17.
 */

public class DataProvider {

    private AppDBHelper appDBHelper;
    private Context context;

    public DataProvider(Context context) {
        this.appDBHelper = new AppDBHelper(context, AppDBContract.DB_NAME,
                null, AppDBContract.DB_VERSION);
        this.context = context;
    }

    public void addForecast(Forecast forecast) {
        SQLiteDatabase db = appDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Calendar calendar = Calendar.getInstance();

        contentValues.put(AppDBContract.ForecastEntries.COLUMN_TEXT, forecast.getText());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_BUSINESS, forecast.getValueBusiness());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_LOVE, forecast.getValueLove());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_HEALTH, forecast.getValueHealth());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_TYPE, forecast.getForecastType());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_SIGN, forecast.getSign());

    }

    public void getForecast(String type, String sign) {
        SQLiteDatabase db = appDBHelper.getReadableDatabase();

    }

    public void deleteForecast() {
        SQLiteDatabase db = appDBHelper.getWritableDatabase();

    }

}
