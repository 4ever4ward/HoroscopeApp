package ua.matvienko_apps.horoscope.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    public static String APP_PREFERENCES;
    public static final String HAS_VISITED = "HasVisited";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Intent intent = null;

//        if(!sharedPreferences.getBoolean(HAS_VISITED, false)) {
//            editor.putBoolean(HAS_VISITED, true);
//            editor.apply();

            intent = new Intent(this, HelloActivity.class);

//        } else {

//            intent = new Intent(this, MainActivity.class);
//        }

        startActivity(intent);
        finish();

    }
}
