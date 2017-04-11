package ua.matvienko_apps.horoscope.classes;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import ua.matvienko_apps.horoscope.R;

public class TimePreference extends DialogPreference {
    private int lastHour = 0;
    private int lastMinute = 0;
    private TimePicker picker = null;

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return li.inflate(R.layout.time_preference, parent, false);
    }

    private static int getHour(String time) {
        String[] pieces = time.split(":");

        return (Integer.parseInt(pieces[0]));
    }

    private static int getMinute(String time) {
        String[] pieces = time.split(":");

        return (Integer.parseInt(pieces[1]));
    }

    public TimePreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);

        setPositiveButtonText("Готово");
        setNegativeButtonText("Выйти");
    }

    @Override
    protected View onCreateDialogView() {
        picker = new TimePicker(getContext());
        picker.setIs24HourView(true);

        return (picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        picker.setCurrentHour(lastHour);
        picker.setCurrentMinute(lastMinute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            lastHour = picker.getCurrentHour();
            lastMinute = picker.getCurrentMinute();

            String time = String.valueOf(lastHour) + ":" + String.valueOf(lastMinute);

            if (callChangeListener(time)) {
                persistString(time);
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
        String time;

        if (restoreValue) {
            if (defaultValue == null) {
                time = getPersistedString("00:00");
            } else {
                time = getPersistedString(defaultValue.toString());
            }
        } else {
            time = defaultValue.toString();
        }

        lastHour = getHour(time);
        lastMinute = getMinute(time);

        setSummary(getSummary());

    }

    @Override
    public CharSequence getSummary() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, lastHour, lastMinute);
        return DateFormat.getTimeFormat(getContext()).format(new Date(cal.getTimeInMillis()));
    }
}