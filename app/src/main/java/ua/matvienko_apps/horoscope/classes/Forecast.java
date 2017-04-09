package ua.matvienko_apps.horoscope.classes;

/**
 * Created by Alexandr on 01/04/2017.
 */

public class Forecast {

    public static final String TODAY = "today";
    public static final String TOMORROW = "tomorrow";
    public static final String WEEK = "week";
    public static final String MONTH = "month";
    public static final String YEAR = "year";

    public static final String AQUARIUS = "aquarius";
    public static final String PISCES = "pisces";
    public static final String ARIES = "aries";
    public static final String TAURUS = "taurus";
    public static final String GEMINI = "gemini";
    public static final String CANCER = "cancer";
    public static final String LEO = "leo";
    public static final String VIRGO = "virgo";
    public static final String LIBRA = "libra";
    public static final String SCORPIO = "scorpio";
    public static final String SAGITTARIUS = "sagittarius";
    public static final String CAPRICORN = "capricorn";

    private String text;
    private int valueBusiness;
    private int valueLove;
    private int valueHealth;
    private String forecastPeriod;
    private String sign;


    public Forecast(String text, int valueBusiness, int valueLove, int valueHealth, String forecastPeriod, String sign) {
        this.text = text;
        this.valueBusiness = valueBusiness;
        this.valueLove = valueLove;
        this.valueHealth = valueHealth;
        this.forecastPeriod = forecastPeriod;
        this.sign = sign;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public int getValueBusiness() {
        return valueBusiness;
    }
    public void setValueBusiness(int valueBusiness) {
        this.valueBusiness = valueBusiness;
    }
    public int getValueLove() {
        return valueLove;
    }
    public void setValueLove(int valueLove) {
        this.valueLove = valueLove;
    }
    public int getValueHealth() {
        return valueHealth;
    }
    public void setValueHealth(int valueHealth) {
        this.valueHealth = valueHealth;
    }
    public String getForecastPeriod() {
        return forecastPeriod;
    }
    public void setForecastPeriod(String forecastPeriod) {
        this.forecastPeriod = forecastPeriod;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
}
