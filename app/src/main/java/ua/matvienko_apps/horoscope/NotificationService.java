package ua.matvienko_apps.horoscope;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

import ua.matvienko_apps.horoscope.activities.MainActivity;
import ua.matvienko_apps.horoscope.classes.Forecast;
import ua.matvienko_apps.horoscope.data.DataProvider;


public class NotificationService extends IntentService {

    public static int NotificationID = 133;

    private static final String TAG = NotificationService.class.getSimpleName();
    private Forecast forecast;

    private static Calendar calendar;

    public NotificationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String dob = preferences.getString("birthday_date", "01.01.1990");

        int day = Integer.parseInt(dob.split("\\.")[0]);
        int month = Integer.parseInt(dob.split("\\.")[1]) + 1;


        DataProvider dataProvider = new DataProvider(getApplicationContext());

        forecast = dataProvider.getForecast(Forecast.TODAY, Utility.getZodiacName(month, day));



        if (calendar.get(Calendar.MINUTE) == Calendar.getInstance().get(Calendar.MINUTE) &&
                calendar.get(Calendar.HOUR_OF_DAY) == Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
            showNotification(forecast);
    }

    private void showNotification(Forecast forecast) {

        int requestID = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, 0);


        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(getString(R.string.app_name) + ": " + "Forecast notification")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(forecast.getText())
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NotificationID, notification);
    }

    public static void setServiceAlarm(Context context, boolean isOn, Calendar time) {
        Intent intent = new Intent(context, NotificationService.class);
        calendar = time;
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if (isOn) {
            alarmManager.cancel(pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), 24*60*60*1000 , pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}

