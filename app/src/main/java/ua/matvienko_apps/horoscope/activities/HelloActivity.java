package ua.matvienko_apps.horoscope.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import ua.matvienko_apps.horoscope.R;

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

                Intent intent = new Intent(HelloActivity.this, MainActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                startActivity(intent);
                finish();

            }
        });

    }
}
