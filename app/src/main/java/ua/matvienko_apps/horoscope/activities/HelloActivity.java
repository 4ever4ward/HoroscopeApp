package ua.matvienko_apps.horoscope.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import java.lang.reflect.Field;
import java.util.Calendar;

import ua.matvienko_apps.horoscope.NotificationService;
import ua.matvienko_apps.horoscope.R;
import ua.matvienko_apps.horoscope.Utility;
import ua.matvienko_apps.horoscope.data.DataProvider;

public class HelloActivity extends AppCompatActivity {

    private DatePicker brdDatePicker;
    SharedPreferences prefs;

    public static final String BRD_YEAR = "year";
    public static final String BRD_MONTH = "month";
    public static final String BRD_DAY = "day";
    public static final String SIGN = "sign";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        prefs = PreferenceManager.getDefaultSharedPreferences(HelloActivity.this);
        SharedPreferences sharedPreferences = getSharedPreferences(SplashActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        brdDatePicker = (DatePicker) findViewById(R.id.brdDatePicker);
        final Button nextBtn = (Button) findViewById(R.id.nextBtn);

        colorizeDatePicker(brdDatePicker);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = brdDatePicker.getYear();
                int month = brdDatePicker.getMonth();
                int day = brdDatePicker.getDayOfMonth();


                editor.putInt(BRD_YEAR, year);
                editor.putInt(BRD_MONTH, month);
                editor.putInt(BRD_DAY, day);

                editor.putString(SIGN, Utility.getZodiacName(month, day));

                editor.apply();


                new SignIn(HelloActivity.this, year, month, day).execute();

            }
        });


    }

    private class SignIn extends AsyncTask<Void, Void, Void> {

        private Context context;
        private DataProvider dataProvider;
        private int brd_year;
        private int brd_month;
        private int brd_day;

        private boolean signed;
        private boolean visited;

        SignIn(Context context, int brd_year, int brd_month, int brd_day) {
            this.context = context;
            this.dataProvider = new DataProvider(context);
            this.brd_year = brd_year;
            this.brd_month = brd_month;
            this.brd_day = brd_day;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(brd_year, brd_month - 1, brd_day);

            signed = dataProvider.signIn(Utility.getZodiacName(brd_month, brd_day),
                    calendar.getTime());


            prefs.edit().putString(getString(R.string.pref_birthday_date),
                    brd_day + "." + (brd_month) + "." + brd_year).apply();


            visited = dataProvider.isVisited();

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (signed || visited) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);


                //Start notification at spec time
                String dobStr = prefs.getString(getString(R.string.pref_notification_time), "8:30");
                String[] dobArr = dobStr.split(":");

                Calendar time = Calendar.getInstance();

                time.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dobArr[0]));
                time.set(Calendar.MINUTE, Integer.parseInt(dobArr[1]));
                time.set(Calendar.SECOND, 0);



                NotificationService.setServiceAlarm(HelloActivity.this, true, time);
                /////////////////////////////////////////////////////////////////

                finish();
            }

        }
    }

    public void colorizeDatePicker(DatePicker datePicker) {
        Resources system = Resources.getSystem();
        int dayId = system.getIdentifier("day", "id", "android");
        int monthId = system.getIdentifier("month", "id", "android");
        int yearId = system.getIdentifier("year", "id", "android");

        NumberPicker dayPicker = (NumberPicker) datePicker.findViewById(dayId);
        NumberPicker monthPicker = (NumberPicker) datePicker.findViewById(monthId);
        NumberPicker yearPicker = (NumberPicker) datePicker.findViewById(yearId);

        setDividerColor(dayPicker);
        setDividerColor(monthPicker);
        setDividerColor(yearPicker);
    }

    private void setDividerColor(NumberPicker picker) {
        if (picker == null)
            return;

        final int count = picker.getChildCount();
        for (int i = 0; i < count; i++) {
            try {
                Field dividerField = picker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(HelloActivity.this, R.color.colorAccent));
                dividerField.set(picker, colorDrawable);
                picker.invalidate();
            } catch (Exception e) {
                Log.w("setDividerColor", e);
            }
        }
    }

}
