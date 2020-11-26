package com.z4rtx.weatheria.apis.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {

    @SerializedName("dt")
    private
    long date;

    private
    Main main;

    private
    List<Weather> weather;

    public long getDate() {
        return date;
    }

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }
}
