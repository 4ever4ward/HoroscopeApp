package ua.matvienko_apps.horoscope.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ua.matvienko_apps.horoscope.R;
import ua.matvienko_apps.horoscope.data.DataProvider;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private int brd_year;
    private int brd_month;
    private int brd_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        brd_year = getIntent().getIntExtra("year", 0);
        brd_month = getIntent().getIntExtra("month", 0);
        brd_day = getIntent().getIntExtra("day", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        new Sync().execute();

    }


    public class Sync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
//            new DataProvider(MainActivity.this).
//                    signIn(Utility.getUUID(MainActivity.this),
//                            Utility.getZodiacName(brd_month, brd_day),
//                            brd_day + "." + brd_month + "." + brd_year);

            new DataProvider(MainActivity.this).getForecast();

            return null;
        }
    }










    public static class ForecastFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public ForecastFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ForecastFragment newInstance(int sectionNumber) {
            ForecastFragment fragment = new ForecastFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            final TextView forecastTextView = (TextView) rootView.findViewById(R.id.forecast_text);
            ImageView copyToClipBoardView = (ImageView) rootView.findViewById(R.id.copy_to_clipboard_btn);
            ImageView shareView = (ImageView) rootView.findViewById(R.id.share_btn);

            shareView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContext().startActivity(createShareForecastIntent(forecastTextView.getText().toString(), "#" + getResources().getString(R.string.app_name)));
                }
            });

            copyToClipBoardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("", forecastTextView.getText().toString());
                    clipboard.setPrimaryClip(clip);

//                    ((ImageView) v).setImageResource(R.drawable.ic_copyf);

                    //TODO: show toast
                }
            });

            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));


            return rootView;
        }

        private Intent createShareForecastIntent(String text, String HASH_TAG) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, text + HASH_TAG);

            return shareIntent;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a ForecastFragment (defined as a static inner class below).
            return ForecastFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {

                //TODO: Change to string resources

                case 0:
                    return "СЕГОДНЯ";
                case 1:
                    return "ЗАВТРА";
                case 2:
                    return "НЕДЕЛЯ";
                case 3:
                    return "МЕСЯЦ";
                case 4:
                    return "ГОД";


            }
            return null;
        }
    }
}
