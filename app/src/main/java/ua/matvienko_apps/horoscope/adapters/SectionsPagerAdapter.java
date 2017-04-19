package ua.matvienko_apps.horoscope.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ua.matvienko_apps.horoscope.classes.Forecast;
import ua.matvienko_apps.horoscope.ForecastFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {
        String period = getPeriodName(position);

        return new ForecastFragment().newInstance(period);
    }


    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

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

    private String getPeriodName(int position) {
        String period;
        switch (position) {
            case 0:
                period = Forecast.TODAY;
                break;
            case 1:
                period = Forecast.TOMORROW;
                break;
            case 2:
                period = Forecast.WEEK;
                break;
            case 3:
                period = Forecast.MONTH;
                break;
            case 4:
                period = Forecast.YEAR;
                break;
            default:
                period = Forecast.TODAY;
                break;

        }

        return period;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
