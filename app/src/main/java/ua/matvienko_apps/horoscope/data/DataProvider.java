package ua.matvienko_apps.horoscope.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ua.matvienko_apps.horoscope.Utility;
import ua.matvienko_apps.horoscope.classes.Forecast;

/**
 * Created by alex_ on 04-Apr-17.
 */

public class DataProvider {

    private AppDBHelper appDBHelper;
    private Context context;

    private final String API_URL = "http://app2.app.trafficterminal.com/api/";
    private final String PARAM_UUID = "uuid";
    private final String PARAM_SIGN = "sign";
    private final String PARAM_DOB = "dob";
    private final String PARAM_NOT_TIME = "notification_time";
    private final String PARAM_NOT_STATUS = "notification_status";


    public static final String SIGN = "sign";
    public static final String NOT_TIME = "notification_time";
    public static final String NOT_STATUS = "notification_status";
    public static final String DOB = "dob";

    private final String RESPONSE = "response";

    public DataProvider(Context context) {
        this.appDBHelper = new AppDBHelper(context, AppDBContract.DB_NAME,
                null, AppDBContract.DB_VERSION);
        this.context = context;
    }

    public boolean isVisited() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(API_URL + "horoscop");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair(PARAM_UUID, Utility.getUUID(context)));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpClient.execute(httpPost);
            String forecastJsonStr = inputStreamToString(response.getEntity().getContent());

            return getSuccess(forecastJsonStr);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return isTableEmpty();
    }

    public void changeSettings(String sign, String dob, String not_time, String not_status) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(API_URL + "settings");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair(PARAM_UUID, Utility.getUUID(context)));
            nameValuePairs.add(new BasicNameValuePair(PARAM_SIGN, sign));
            nameValuePairs.add(new BasicNameValuePair(PARAM_DOB, dob));
            nameValuePairs.add(new BasicNameValuePair(PARAM_NOT_TIME, not_time));
            nameValuePairs.add(new BasicNameValuePair(PARAM_NOT_STATUS, not_status));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpClient.execute(httpPost);
            String settingsJsonStr = inputStreamToString(response.getEntity().getContent());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSettings(String param) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(API_URL + "horoscop");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair(PARAM_UUID, Utility.getUUID(context)));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpClient.execute(httpPost);
            String settingsJsonStr = inputStreamToString(response.getEntity().getContent());

            return parseSettingsJson(settingsJsonStr, param);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String parseSettingsJson(String settingsJsonString, String param) throws JSONException {
        final String SETTINGS = "settings";

        JSONObject settingsJsonObj = new JSONObject(settingsJsonString)
                .getJSONObject(RESPONSE)
                .getJSONObject(SETTINGS);

        return settingsJsonObj.getString(param);

    }

    public Forecast getForecast(String period, String sign) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(API_URL + "horoscop");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair(PARAM_UUID, Utility.getUUID(context)));
            nameValuePairs.add(new BasicNameValuePair(PARAM_SIGN, sign));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


            HttpResponse response = httpClient.execute(httpPost);
            String forecastJsonStr = inputStreamToString(response.getEntity().getContent());

            parseForecastJson(forecastJsonStr, sign);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getForecastFromDB(period, sign);
    }

    public boolean signIn(String sign, Date brd_date) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(API_URL + "horoscop");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair(PARAM_UUID, Utility.getUUID(context)));
            nameValuePairs.add(new BasicNameValuePair(PARAM_SIGN, sign));
            nameValuePairs.add(new BasicNameValuePair(PARAM_DOB, Utility.normalizeDate(brd_date)));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpClient.execute(httpPost);
            String forecastJsonStr = inputStreamToString(response.getEntity().getContent());

            return getSuccess(forecastJsonStr);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean getSuccess(String forecastJsonStr) throws JSONException {
        String SUCCESS = "success";
        int success_value = new JSONObject(forecastJsonStr).getInt(SUCCESS);

        return success_value == 1;
    }

    private void parseForecastJson(String forecastJsonStr, String sign) throws JSONException {

        final String YEAR = "year";
        final String MONTH = "month";
        final String WEEK = "week";
        final String TOMORROW = "tomorrow";
        final String TODAY = "today";


        String HOROSCOP = "horoscop";
        JSONObject horoscopJsonObj = new JSONObject(forecastJsonStr)
                .getJSONObject(RESPONSE)
                .getJSONObject(HOROSCOP);

        Log.e("fsdfsf", "parseForecastJson: " + horoscopJsonObj.getJSONObject(TODAY).getString("text"));

        readForecastObj(horoscopJsonObj.getJSONObject(YEAR), YEAR, sign);
        readForecastObj(horoscopJsonObj.getJSONObject(MONTH), MONTH, sign);
        readForecastObj(horoscopJsonObj.getJSONObject(WEEK), WEEK, sign);
        readForecastObj(horoscopJsonObj.getJSONObject(TOMORROW), TOMORROW, sign);
        readForecastObj(horoscopJsonObj.getJSONObject(TODAY), TODAY, sign);


    }

    private void readForecastObj(JSONObject forecast, String forecastPeriod, String sign) throws JSONException {
        final String TEXT = "text";
        final String BUSINESS_VALUE = "business_rating";
        final String LOVE_VALUE = "heart_rating";
        final String HEALTH_VALUE = "health_rating";

        String forecastText = forecast.getString(TEXT);
        int businessValue = forecast.getInt(BUSINESS_VALUE);
        int loveValue = forecast.getInt(LOVE_VALUE);
        int healthValue = forecast.getInt(HEALTH_VALUE);

        if (getForecastFromDB(forecastPeriod, sign) == null) {
            addForecast(new Forecast(forecastText, businessValue, loveValue,
                    healthValue, forecastPeriod, sign));
        } else {
            updateForecast(new Forecast(forecastText, businessValue, loveValue,
                    healthValue, forecastPeriod, sign));
        }

    }

    private void addForecast(Forecast forecast) {
        SQLiteDatabase db = appDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(AppDBContract.ForecastEntries.COLUMN_TEXT, forecast.getText());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_BUSINESS, forecast.getValueBusiness());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_LOVE, forecast.getValueLove());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_HEALTH, forecast.getValueHealth());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_PERIOD, forecast.getForecastPeriod());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_SIGN, forecast.getSign());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_DATE, Utility.getNowDateString());

        db.insert(AppDBContract.ForecastEntries.TABLE_NAME, null, contentValues);
        db.close();

    }

    private Forecast getForecastFromDB (String period, String sign) {
        SQLiteDatabase db = appDBHelper.getReadableDatabase();
        String query = "SELECT * FROM "
                + AppDBContract.ForecastEntries.TABLE_NAME
                + " WHERE "
                + AppDBContract.ForecastEntries.COLUMN_PERIOD + " = " + "\"" + period + "\""
                + " AND "
                + AppDBContract.ForecastEntries.COLUMN_SIGN + " = " + "\"" + sign + "\"";

        Cursor cursor = db.rawQuery(query, null);

        Forecast forecast = null;

        if (cursor.moveToFirst()) {
            forecast = new Forecast(cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6));
        }

        db.close();
        cursor.close();
        return forecast;
    }

    private boolean isTableEmpty() {
        SQLiteDatabase db = appDBHelper.getReadableDatabase();
        String query = "SELECT * FROM "
                + AppDBContract.ForecastEntries.TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return true;
        }

        db.close();
        cursor.close();
        return false;
    }

    private void updateForecast(Forecast newForecast) {
        SQLiteDatabase db = appDBHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        if (newForecast.getForecastPeriod().equals(Forecast.TODAY));

        contentValues.put(AppDBContract.ForecastEntries.COLUMN_TEXT, newForecast.getText());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_BUSINESS, newForecast.getValueBusiness());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_LOVE, newForecast.getValueLove());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_HEALTH, newForecast.getValueHealth());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_PERIOD, newForecast.getForecastPeriod());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_SIGN, newForecast.getSign());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_DATE, Utility.getNowDateString());
//        contentValues.put(AppDBContract.ForecastEntries.COLUMN_DATE, newForecast.getAddedDate());


        db.update(AppDBContract.ForecastEntries.TABLE_NAME, contentValues,
                AppDBContract.ForecastEntries.COLUMN_PERIOD + " = ? AND "
                        + AppDBContract.ForecastEntries.COLUMN_SIGN + " = ? ", new String[]{
                        newForecast.getForecastPeriod(),
                        newForecast.getSign()});

        db.close();
    }

    private String inputStreamToString(InputStream is) throws IOException {
        String line;
        StringBuilder total = new StringBuilder();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

        while ((line = bufferedReader.readLine()) != null)
            total.append(line);

        return total.toString();
    }

}
