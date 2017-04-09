package ua.matvienko_apps.horoscope.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ua.matvienko_apps.horoscope.data.DataProvider;

public class SplashActivity extends AppCompatActivity {

    public static String TAG = SplashActivity.class.getSimpleName();

    public static String APP_PREFERENCES;
    public static final String HAS_VISITED = "HasVisited";

    private boolean visited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        new isVisited(SplashActivity.this).execute();

    }


    private class isVisited extends AsyncTask<Void, Void, Void> {

        private Context context;
        private DataProvider dataProvider;

        public isVisited(Context context) {
            this.context = context;
            dataProvider = new DataProvider(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            visited = dataProvider.isVisited();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent;

            if (visited) {
                intent = new Intent(context, MainActivity.class);
            } else {
                intent = new Intent(context, HelloActivity.class);
            }

            startActivity(intent);
            finish();

        }
    }




}
