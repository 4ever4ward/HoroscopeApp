package ua.matvienko_apps.horoscope.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Arrays;
import java.util.Calendar;

import ua.matvienko_apps.horoscope.NotificationService;
import ua.matvienko_apps.horoscope.R;
import ua.matvienko_apps.horoscope.SignSpinnerAdapter;
import ua.matvienko_apps.horoscope.Utility;
import ua.matvienko_apps.horoscope.adapters.SectionsPagerAdapter;
import ua.matvienko_apps.horoscope.classes.Forecast;
import ua.matvienko_apps.horoscope.data.DataProvider;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Spinner signSpinner;

    private String userSign;
    private SharedPreferences prefs;
    private AdView adView;


    String app_link;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load an ad into the AdMob banner view.
        adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);

        SharedPreferences sharedPreferences = getSharedPreferences(SplashActivity.APP_PREFERENCES, Context.MODE_PRIVATE);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView settingsIcon = (ImageView) findViewById(R.id.settings_icon);

        signSpinner = (Spinner) findViewById(R.id.sign_spinner);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        signSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSectionsPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        signSpinner.setAdapter(new SignSpinnerAdapter(this,
                Arrays.asList(getResources().getStringArray(R.array.spinner_signs))));

        userSign = sharedPreferences.getString(HelloActivity.SIGN, "");

    }

    private int getZodiacPosition(String name) {
        int signPosition = -1;

        switch (name) {
            case Forecast.AQUARIUS:
                signPosition = 0;
                break;
            case Forecast.PISCES:
                signPosition = 1;
                break;
            case Forecast.ARIES:
                signPosition = 2;
                break;
            case Forecast.TAURUS:
                signPosition = 3;
                break;
            case Forecast.GEMINI:
                signPosition = 4;
                break;
            case Forecast.CANCER:
                signPosition = 5;
                break;
            case Forecast.LEO:
                signPosition = 6;
                break;
            case Forecast.VIRGO:
                signPosition = 7;
                break;
            case Forecast.LIBRA:
                signPosition = 8;
                break;
            case Forecast.SCORPIO:
                signPosition = 9;
                break;
            case Forecast.SAGITTARIUS:
                signPosition = 10;
                break;
            case Forecast.CAPRICORN:
                signPosition = 11;
                break;
        }

        return signPosition;
    }

    private String formatDateString(String d_str) {
        String[] new_str_arr = d_str.replace("-", ".").split("\\.");

        return new_str_arr[2] + "." + (Integer.parseInt(new_str_arr[1])) + "." + new_str_arr[0];
    }

    private class syncSettings extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor editor = prefs.edit();

            DataProvider dataProvider = new DataProvider(MainActivity.this);

            String dob_str = dataProvider.getSettings(DataProvider.DOB);

            if (dob_str.equals(""))
                return null;

            String not_time = dataProvider.getSettings(DataProvider.NOT_TIME);
            app_link = dataProvider.getSettings(DataProvider.APP_LINK);

            int not_status = Integer.parseInt(dataProvider.getSettings(DataProvider.NOT_STATUS));

            if (not_status == 1) {
                editor.putBoolean(getString(R.string.pref_notification_switch), true);
            } else if (not_status == 0) {
                editor.putBoolean(getString(R.string.pref_notification_switch), false);
            }
            editor.putString(getString(R.string.pref_notification_time), not_time);
            editor.putString(getString(R.string.pref_birthday_date), formatDateString(dob_str));
            editor.putString(getString(R.string.pref_app_link), app_link);

            editor.apply();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String date = prefs.getString(getString(R.string.pref_birthday_date), "01.01.1990");

            int month = Integer.parseInt(date.split("\\.")[1]) + 1;
            int day = Integer.parseInt(date.split("\\.")[0]);

            signSpinner.setSelection(getZodiacPosition(Utility.getZodiacName(month, day)));
        }
    }

    private class getZodiac extends AsyncTask<Void, Void, Void> {

        Context context;

        public getZodiac(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            userSign = new DataProvider(context).getSettings(DataProvider.SIGN);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            signSpinner.setSelection(getZodiacPosition(userSign));

            super.onPostExecute(aVoid);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Sync Settings with api
        new syncSettings().execute();

        String date = prefs.getString(getString(R.string.pref_birthday_date), "01.01.1990");

        int month = Integer.parseInt(date.split("\\.")[1]) + 1;
        int day = Integer.parseInt(date.split("\\.")[0]);

        String dobStr = prefs.getString(getString(R.string.pref_notification_time), "8:30");
        String[] dobArr = dobStr.split(":");

        Calendar time = Calendar.getInstance();

        time.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dobArr[0]));
        time.set(Calendar.MINUTE, Integer.parseInt(dobArr[1]));
        time.set(Calendar.SECOND, 0);

        NotificationService.setServiceAlarm(MainActivity.this, true, time);

        signSpinner.setSelection(getZodiacPosition(Utility.getZodiacName(month, day)));

    }
}
