package ua.matvienko_apps.horoscope.activities;

import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

import ua.matvienko_apps.horoscope.NotificationService;
import ua.matvienko_apps.horoscope.R;
import ua.matvienko_apps.horoscope.Utility;
import ua.matvienko_apps.horoscope.data.DataProvider;

/**
 * Created by Alexandr on 05/04/2017.
 */

public class SettingsActivity extends PreferenceActivity {

    private AppCompatDelegate mDelegate;
    private AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Load an ad into the AdMob banner view.
        adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);

        ImageView cancelImage = (ImageView) findViewById(R.id.cancel_action);
        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getFragmentManager().beginTransaction()
                .replace(R.id.container, new PrefsFragment()).commit();

        PreferenceManager.getDefaultSharedPreferences(this).
                registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);

                        String dob_str = preferences.getString(getString(R.string.pref_birthday_date), "01.01.1990");

                        String not_time_str = preferences.getString(getString(R.string.pref_notification_time), "8:30");
                        String not_status = "1";

                        if (!preferences.getBoolean(getString(R.string.pref_notification_switch), true))
                            not_status = "0";

                        int day = Integer.parseInt(dob_str.split("\\.")[0]);
                        int month = Integer.parseInt(dob_str.split("\\.")[1]) - 1;

                        String sign = Utility.getZodiacName(month, day);

                        new changeSettings(sign, dob_str, not_time_str, not_status).execute();

                        switch (key) {
                            case "notification_switch":

                                //Start notification at spec time
                                String timeStr = preferences.getString(getString(R.string.pref_notification_time), "8:30");
                                String[] timeArr = timeStr.split(":");

                                Calendar calendar = Calendar.getInstance();

                                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArr[0]));
                                calendar.set(Calendar.MINUTE, Integer.parseInt(timeArr[1]));
                                calendar.set(Calendar.SECOND, 0);

                                if (!preferences.getBoolean(getString(R.string.pref_notification_switch), true)) {
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    notificationManager.cancelAll();
                                } else {
                                    NotificationService.setServiceAlarm(SettingsActivity.this, true, calendar);
                                }
                                break;

                            case "notification_time":

                                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                notificationManager.cancelAll();

                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);

                                //Start notification at spec time
                                String dobStr = prefs.getString(getString(R.string.pref_notification_time), "8:30");
                                String[] dobArr = dobStr.split(":");

                                Calendar time = Calendar.getInstance();

                                time.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dobArr[0]));
                                time.set(Calendar.MINUTE, Integer.parseInt(dobArr[1]));
                                time.set(Calendar.SECOND, 0);


                                NotificationService.setServiceAlarm(SettingsActivity.this, true, time);
                                /////////////////////////////////////////////////////////////////

                                break;

                        }

                    }
                });
    }

    public class PrefsFragment extends PreferenceFragment {


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            addPreferencesFromResource(R.xml.app_preferences);

        }
    }

    private void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

    private class changeSettings extends AsyncTask<Void, Void, Void> {

        private String sign;
        private String dob_str;
        private String not_time_str;
        private String not_status;

        public changeSettings(String sign, String dob_str, String not_time_str, String not_status) {
            this.sign = sign;
            this.dob_str = dob_str;
            this.not_time_str = not_time_str;
            this.not_status = not_status;
        }

        @Override
        protected Void doInBackground(Void... params) {

            new DataProvider(SettingsActivity.this).changeSettings(sign, dob_str, not_time_str, not_status);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

}
