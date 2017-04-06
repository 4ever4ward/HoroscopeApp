package ua.matvienko_apps.horoscope;

import android.content.Context;
import android.provider.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Alexandr on 04/04/2017.
 */

public class Utility {

    public static String getUUID(Context context) {
        String uuid;

        String device_uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String bundle_id = context.getPackageName();
        String user_agent = System.getProperty("http.agent");
        String time_zone = TimeZone.getDefault().getID();
        String device_locale = Locale.getDefault().getLanguage() + "-" +Locale.getDefault().getCountry();
        String device_country = context.getResources().getConfiguration().locale.getCountry();

        uuid = "{ "
                + "\"bundle_id\":" +  "\"" + bundle_id + "\"" + ","
                //TODO: change hardcoded string to device_uuid
                + "\"unique_id\":" + "\"" + "11111111" + "\"" + ","
                + "\"user_agent\":" + "\"" + user_agent + "\"" + ","
                + "\"time_zone\":" + "\"" + time_zone + "\"" + ","
                + "\"device_locale\":" + "\"" + device_locale + "\"" + ","
                + "\"device_country\":" + "\"" + device_country + "\""
                + " }";

        return uuid;
    }

    public static String getNowDateString() {
        return new SimpleDateFormat("dd.MM.yyyy").format(new Date());
    }

    public static String normalizeDate(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy").format(date);
    }

    public static String getZodiacName(int brd_month, int brd_day) {

        // TODO: change string's to real constant's
        if      ((brd_month == 12 && brd_day >= 22 && brd_day <= 31) || (brd_month ==  1 && brd_day >= 1 && brd_day <= 19))
            return "capricorn";
        else if ((brd_month ==  1 && brd_day >= 20 && brd_day <= 31) || (brd_month ==  2 && brd_day >= 1 && brd_day <= 17))
            return "aquarius";
        else if ((brd_month ==  2 && brd_day >= 18 && brd_day <= 29) || (brd_month ==  3 && brd_day >= 1 && brd_day <= 19))
            return "pisces";
        else if ((brd_month ==  3 && brd_day >= 20 && brd_day <= 31) || (brd_month ==  4 && brd_day >= 1 && brd_day <= 19))
            return "aries";
        else if ((brd_month ==  4 && brd_day >= 20 && brd_day <= 30) || (brd_month ==  5 && brd_day >= 1 && brd_day <= 20))
            return "taurus";
        else if ((brd_month ==  5 && brd_day >= 21 && brd_day <= 31) || (brd_month ==  6 && brd_day >= 1 && brd_day <= 20))
            return "gemini";
        else if ((brd_month ==  6 && brd_day >= 21 && brd_day <= 30) || (brd_month ==  7 && brd_day >= 1 && brd_day <= 22))
            return "cancer";
        else if ((brd_month ==  7 && brd_day >= 23 && brd_day <= 31) || (brd_month ==  8 && brd_day >= 1 && brd_day <= 22))
            return "leo";
        else if ((brd_month ==  8 && brd_day >= 23 && brd_day <= 31) || (brd_month ==  9 && brd_day >= 1 && brd_day <= 22))
            return "virgo";
        else if ((brd_month ==  9 && brd_day >= 23 && brd_day <= 30) || (brd_month == 10 && brd_day >= 1 && brd_day <= 22))
            return "libra";
        else if ((brd_month == 10 && brd_day >= 23 && brd_day <= 31) || (brd_month == 11 && brd_day >= 1 && brd_day <= 21))
            return "scorpio";
        else if ((brd_month == 11 && brd_day >= 22 && brd_day <= 30) || (brd_month == 12 && brd_day >= 1 && brd_day <= 21))
            return "sagittarius";

        return "";
    }


}
