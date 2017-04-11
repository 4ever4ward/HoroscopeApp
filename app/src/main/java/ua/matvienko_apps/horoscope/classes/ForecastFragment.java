package ua.matvienko_apps.horoscope.classes;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import ua.matvienko_apps.horoscope.R;
import ua.matvienko_apps.horoscope.data.DataProvider;



public class ForecastFragment extends Fragment {

    private static final String ARG_PERIOD = "period";

    private TextView forecastTextView;
    private TextView businessTextView;
    private TextView loveTextView;
    private TextView healthTextView;

    private ProgressBar businessProgressBar;
    private ProgressBar loveProgressBar;
    private ProgressBar healthProgressBar;

    public ForecastFragment() {
    }

    public ForecastFragment newInstance(String period) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PERIOD, period);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        forecastTextView = (TextView) rootView.findViewById(R.id.forecast_text);
        businessTextView = (TextView) rootView.findViewById(R.id.business_text_value);
        loveTextView = (TextView) rootView.findViewById(R.id.love_text_value);
        healthTextView = (TextView) rootView.findViewById(R.id.health_text_value);

        businessProgressBar = (ProgressBar) rootView.findViewById(R.id.business_progress_bar);
        loveProgressBar = (ProgressBar) rootView.findViewById(R.id.love_progress_bar);
        healthProgressBar = (ProgressBar) rootView.findViewById(R.id.health_progress_bar);


        ImageView copyToClipBoardView = (ImageView) rootView.findViewById(R.id.copy_to_clipboard_btn);
        ImageView shareView = (ImageView) rootView.findViewById(R.id.share_btn);

        String sign =
                getZodiacName(((Spinner) getActivity().findViewById(R.id.sign_spinner))
                        .getSelectedItemPosition());

        String period = getArguments().getString(ARG_PERIOD);

        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(createShareForecastIntent(forecastTextView.getText().toString(), "#" + getResources().getString(R.string.app_name)));
            }
        });

        copyToClipBoardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View layout = inflater.inflate(R.layout.toast_layout, null);

                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", forecastTextView.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast toast = new Toast(getContext());
                toast.setGravity(Gravity.BOTTOM, 0, 330);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }
        });

//        Log.e(TAG, "onCreateView: " + ((Spinner) getActivity().findViewById(R.id.sign_spinner)).getSelectedItemPosition());

        new getForecastTask(getContext(), period, sign).execute();

        return rootView;
    }


    private class getForecastTask extends AsyncTask<Void, Void, Void> {

        private DataProvider dataProvider;
        private Forecast forecast;
        private String sign;
        private String period;

        getForecastTask(Context context, String period, String sign) {
            dataProvider = new DataProvider(context);
            this.period = period;
            this.sign = sign;
        }

        @Override
        protected Void doInBackground(Void... params) {
            forecast = dataProvider.getForecast(period, sign);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (forecast != null) {
                forecastTextView.setText(forecast.getText());
                businessTextView.setText(String.format(Locale.getDefault(), "%d", forecast.getValueBusiness()));
                loveTextView.setText(String.format(Locale.getDefault(), "%d", forecast.getValueLove()));
                healthTextView.setText(String.format(Locale.getDefault(), "%d", forecast.getValueHealth()));

                businessProgressBar.setProgress(forecast.getValueBusiness());
                loveProgressBar.setProgress(forecast.getValueLove());
                healthProgressBar.setProgress(forecast.getValueHealth());

            } else {
                forecastTextView.setText("Отсутствует подключение к Интернету");

                businessProgressBar.setProgress(0);
                loveProgressBar.setProgress(0);
                healthProgressBar.setProgress(0);
            }

        }
    }

    private String getZodiacName(int position) {
        String signName = null;

        switch (position) {
            case 0:
                signName = Forecast.AQUARIUS;
                break;
            case 1:
                signName = Forecast.PISCES;
                break;
            case 2:
                signName = Forecast.ARIES;
                break;
            case 3:
                signName = Forecast.TAURUS;
                break;
            case 4:
                signName = Forecast.GEMINI;
                break;
            case 5:
                signName = Forecast.CANCER;
                break;
            case 6:
                signName = Forecast.LEO;
                break;
            case 7:
                signName = Forecast.VIRGO;
                break;
            case 8:
                signName = Forecast.LIBRA;
                break;
            case 9:
                signName = Forecast.SCORPIO;
                break;
            case 10:
                signName = Forecast.SAGITTARIUS;
                break;
            case 11:
                signName = Forecast.CAPRICORN;
                break;
        }

        return signName;
    }

    private Intent createShareForecastIntent(String text, String HASH_TAG) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text + HASH_TAG);

        return shareIntent;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
