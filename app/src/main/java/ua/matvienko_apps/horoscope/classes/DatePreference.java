package ua.matvienko_apps.horoscope.classes;



import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;

import ua.matvienko_apps.horoscope.R;
import ua.matvienko_apps.horoscope.Utility;


public class DatePreference extends DialogPreference {

    private int lastDay = 1;
    private int lastMonth = 1;
    private int lastYear = 2000;
    private DatePicker picker = null;

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return li.inflate(R.layout.time_preference, parent, false);
    }

    private static int getYear(String date) {
        String[] pieces = date.split("\\.");

        return (Integer.parseInt(pieces[2]));
    }

    private static int getMonth(String date) {
        String[] pieces = date.split("\\.");

        return (Integer.parseInt(pieces[1]));
    }

    private static int getDay(String date) {
        String[] pieces = date.split("\\.");

        return (Integer.parseInt(pieces[0]));
    }

    public DatePreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);
        setPositiveButtonText("Готово");
        setNegativeButtonText("Выйти");

        setDialogTitle("");
    }

    @Override
    protected View onCreateDialogView() {
        picker = new DatePicker(getContext());
        return (picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        picker.init(lastYear, lastMonth, lastDay, null);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
//            lastHour = picker.getCurrentHour();
//            lastMinute = picker.getCurrentMinute();

            lastDay = picker.getDayOfMonth();
            lastMonth = picker.getMonth();
            lastYear = picker.getYear();

            String date = String.valueOf(lastDay) + "." + String.valueOf(lastMonth) + "." + String.valueOf(lastYear);

            if (callChangeListener(date)) {
                persistString(date);
            }
        }

        setSummary(getSummary());

    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String date;

        if (restoreValue) {
            if (defaultValue == null) {
                date = getPersistedString(Utility.getNowDateString());
            } else {
                date = getPersistedString(defaultValue.toString());
            }
        } else {
            date = defaultValue.toString();
        }

        lastYear = getYear(date);
        lastMonth = getMonth(date);
        lastDay = getDay(date);

        setSummary(getSummary());

    }

    @Override
    public CharSequence getSummary() {
        Calendar cal = Calendar.getInstance();
        cal.set(lastYear, lastMonth, lastDay);
        return Utility.normalizeDate(cal.getTime());
    }
}
