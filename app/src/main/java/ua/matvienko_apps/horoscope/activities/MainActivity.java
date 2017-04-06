package ua.matvienko_apps.horoscope.activities;

import android.content.Intent;
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

import ua.matvienko_apps.horoscope.R;
import ua.matvienko_apps.horoscope.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Spinner signSpinner;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView settingsIcon = (ImageView) findViewById(R.id.settings_icon);

        signSpinner = (Spinner) findViewById(R.id.sign_spinner);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        new Sync().execute();

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

//        Log.e(TAG, "onCreate: " + new DataProvider(this).getForecastFromDB("today", "aries").getText());


    }


    public class Sync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
//            new DataProvider(MainActivity.this).
//                    signIn(Utility.getUUID(MainActivity.this),
//                            Utility.getZodiacName(brd_month, brd_day),
//                            brd_day + "." + brd_month + "." + brd_year);

//            Calendar calendar = Calendar.getInstance();
//            calendar.set(brd_year, brd_month, brd_day);
//            calendar.getTime();
//
//            Log.e(TAG, "doInBackground: " + Utility.normalizeDate(calendar.getTime()));
//
//            new DataProvider(MainActivity.this).getForecast("today", "libra");

            return null;
        }
    }


}
