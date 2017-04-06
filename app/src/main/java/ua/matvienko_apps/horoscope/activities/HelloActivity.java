package ua.matvienko_apps.horoscope.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

import ua.matvienko_apps.horoscope.R;
import ua.matvienko_apps.horoscope.Utility;
import ua.matvienko_apps.horoscope.data.DataProvider;

public class HelloActivity extends AppCompatActivity {

    private DatePicker brdDatePicker;

    public static final String BRD_YEAR = "year";
    public static final String BRD_MONTH = "month";
    public static final String BRD_DAY = "day";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        SharedPreferences sharedPreferences = getSharedPreferences(SplashActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        brdDatePicker = (DatePicker) findViewById(R.id.brdDatePicker);
        Button nextBtn = (Button) findViewById(R.id.nextBtn);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = brdDatePicker.getYear();
                int month = brdDatePicker.getMonth();
                int day = brdDatePicker.getDayOfMonth();

                editor.putInt(BRD_YEAR, year);
                editor.putInt(BRD_MONTH, month);
                editor.putInt(BRD_DAY, day);

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

        public SignIn(Context context, int brd_year, int brd_month, int brd_day) {
            this.context = context;
            this.dataProvider = new DataProvider(context);
            this.brd_year = brd_year;
            this.brd_month = brd_month;
            this.brd_day = brd_day;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(brd_year, brd_month, brd_day);

            signed = dataProvider.signIn(Utility.getZodiacName(brd_month, brd_day),
                    calendar.getTime());

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (signed) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }
}
