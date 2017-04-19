package ua.matvienko_apps.horoscope;

import android.app.Application;

import com.flurry.android.FlurryAgent;

/**
 * Created by alex_ on 14-Apr-17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, getString(R.string.flurry_analytics_id));

    }
}
