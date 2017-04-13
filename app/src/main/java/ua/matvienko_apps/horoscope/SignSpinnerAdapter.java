package ua.matvienko_apps.horoscope;

/**
 * Created by Alexandr on 13/04/2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alexandr on 26/02/2017.
 */

public class SignSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context context;
    private List<String> signList;

    public SignSpinnerAdapter(Context context, List<String> signList) {
        this.signList = signList;

        this.context = context;
    }


    public int getCount() {
        return signList.size();
    }

    public Object getItem(int i) {
        return signList.get(i);
    }

    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(context);
        txt.setPadding(25, 25, 25, 25);
        txt.setTextSize(20);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(signList.get(position));
        txt.setTextColor(Color.WHITE);
        txt.setBackgroundResource(R.drawable.spinner_item_selector);
        return txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(context);
        txt.setGravity(Gravity.CENTER);
        txt.setTextSize(20);
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
        txt.setText(signList.get(i));
        txt.setTextColor(Color.WHITE);
        return txt;
    }

}

