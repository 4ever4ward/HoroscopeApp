package ua.matvienko_apps.horoscope;

/**
 * Created by Alexandr on 01/04/2017.
 */

public class Forecast {

    public static final int FOR_TODAY = 101;
    public static final int FOR_TOMORROW = 102;
    public static final int FOR_WEEK = 103;
    public static final int FOR_MONTH = 104;
    public static final int FOR_YEAR = 105;

    public static final int AQUARIUS = 1201;
    public static final int PISCES = 1202;
    public static final int ARIES = 1203;
    public static final int TAURUS = 1204;
    public static final int GEMINI = 1205;
    public static final int CANCER = 1206;
    public static final int LEO = 1207;
    public static final int VIRGO = 1208;
    public static final int LIBRA = 1209;
    public static final int SCORPIO = 1210;
    public static final int SAGITTARIUS = 1211;
    public static final int CAPRICORN = 1212;

    private String text;
    private int valueBusiness;
    private int valueLove;
    private int valueHealth;
    private int forecastType;
    private int sign;


    public Forecast(String text, int valueBusiness, int valueLove, int valueHealth, int forecastType, int sign) {
        this.text = text;
        this.valueBusiness = valueBusiness;
        this.valueLove = valueLove;
        this.valueHealth = valueHealth;
        this.forecastType = forecastType;
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
    public int getForecastType() {
        return forecastType;
    }
    public void setForecastType(int forecastType) {
        this.forecastType = forecastType;
    }
    public int getSign() {
        return sign;
    }
    public void setSign(int sign) {
        this.sign = sign;
    }
}
