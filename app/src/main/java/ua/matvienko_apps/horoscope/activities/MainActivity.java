package ua.matvienko_apps.horoscope.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import ua.matvienko_apps.horoscope.Forecast;
import ua.matvienko_apps.horoscope.R;
import ua.matvienko_apps.horoscope.SectionsPagerAdapter;
import ua.matvienko_apps.horoscope.data.DataProvider;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Spinner signSpinner;

    private String userSign;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(SplashActivity.APP_PREFERENCES, Context.MODE_PRIVATE);

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

        userSign = sharedPreferences.getString(HelloActivity.SIGN, "");

        if (!userSign.equals("")) {
            signSpinner.setSelection(getZodiacPosition(userSign));
        } else {
            new getZodiac(this).execute();
        }


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


}
