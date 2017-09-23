package com.example.android.weatherviewer;

import java.util.Date;

/**
 * Created by 92324 on 2017/9/18.
 */



public class CityWeather {

    private String cityName;
    private String weatherDay;
    private String weatherNight;
    private int tempHigh;
    private int tempLow;
    private int hum;
    private Date date;

    public CityWeather(String cityName, String weatherDay, String weatherNight, int tempHigh, int tempLow, int hum, Date date) {
        this.cityName = cityName;
        this.weatherDay = weatherDay;
        this.weatherNight = weatherNight;
        this.tempHigh = tempHigh;
        this.tempLow = tempLow;
        this.hum = hum;
        this.date = date;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getTempHigh() {
        return tempHigh;
    }

    public void setTempHigh(int tempHigh) {
        this.tempHigh = tempHigh;
    }

    public int getTempLow() {
        return tempLow;
    }

    public void setTempLow(int tempLow) {
        this.tempLow = tempLow;
    }

    public int getHum() {
        return hum;
    }

    public void setHum(int hum) {
        this.hum = hum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getWeatherDay() {
        return weatherDay;
    }

    public void setWeatherDay(String weatherDay) {
        this.weatherDay = weatherDay;
    }

    public String getWeatherNight() {
        return weatherNight;
    }

    public void setWeatherNight(String weatherNight) {
        this.weatherNight = weatherNight;
    }
}
