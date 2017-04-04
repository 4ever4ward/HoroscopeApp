package ua.matvienko_apps.horoscope.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
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
import java.util.List;

import ua.matvienko_apps.horoscope.Forecast;
import ua.matvienko_apps.horoscope.Utility;

import static ua.matvienko_apps.horoscope.activities.MainActivity.TAG;

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


    private final String RESPONSE = "response";
    private final String HOROSCOP = "horoscop";
    private final String SETTINGS = "settings";
    private final String SIGN = "sign";
    private final String SUCCESS = "success";

    public DataProvider(Context context) {
        this.appDBHelper = new AppDBHelper(context, AppDBContract.DB_NAME,
                null, AppDBContract.DB_VERSION);
        this.context = context;
    }

    public void getForecast() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(API_URL + "horoscop");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair(PARAM_UUID, Utility.getUUID(context)));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpClient.execute(httpPost);
            String forecastJsonStr = inputStreamToString(response.getEntity().getContent());
            Log.e(TAG, "getForecast: " + forecastJsonStr.substring(forecastJsonStr.length() - 200));
            parseForecastJson(forecastJsonStr);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void signIn(String uuid, String sign, String brd_date) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(API_URL + "horoscop");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair(PARAM_UUID, Utility.getUUID(context)));
            nameValuePairs.add(new BasicNameValuePair(PARAM_SIGN, sign));
            nameValuePairs.add(new BasicNameValuePair(PARAM_DOB, brd_date));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpClient.execute(httpPost);
            String forecastJsonStr = inputStreamToString(response.getEntity().getContent());
            Log.e(TAG, "signIn: " + forecastJsonStr);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseForecastJson(String forecastJsonStr) throws JSONException {

        final String YEAR = "year";
        final String MONTH = "month";
        final String WEEK = "week";
        final String TOMORROW = "tomorrow";
        final String TODAY = "today";


        JSONObject horoscopJsonObj = new JSONObject(forecastJsonStr)
                .getJSONObject(RESPONSE)
                .getJSONObject(HOROSCOP);

        JSONObject settingsJsonObj = new JSONObject(forecastJsonStr)
                .getJSONObject(RESPONSE)
                .getJSONObject(SETTINGS);

        String sign = settingsJsonObj.getString(SIGN);

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


        Log.e(TAG, "readForecastObj: " + forecastPeriod + ": " + forecastText );

//        addForecast(new Forecast(forecastText, businessValue, loveValue,
//                healthValue, forecastPeriod, sign));

    }


    public void addForecast(Forecast forecast) {
        SQLiteDatabase db = appDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Calendar calendar = Calendar.getInstance();

        contentValues.put(AppDBContract.ForecastEntries.COLUMN_TEXT, forecast.getText());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_BUSINESS, forecast.getValueBusiness());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_LOVE, forecast.getValueLove());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_HEALTH, forecast.getValueHealth());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_PERIOD, forecast.getForecastPeriod());
        contentValues.put(AppDBContract.ForecastEntries.COLUMN_SIGN, forecast.getSign());

    }

    private void getForecastFromDB(String type, String sign) {
        SQLiteDatabase db = appDBHelper.getReadableDatabase();

    }

    private void deleteForecastFromDB() {
        SQLiteDatabase db = appDBHelper.getWritableDatabase();

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
