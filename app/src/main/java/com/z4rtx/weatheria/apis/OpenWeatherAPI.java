package com.z4rtx.weatheria.apis;

import com.z4rtx.weatheria.apis.Responses.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherAPI {

    @GET("2.5/onecall?units=metric&exclude=minutely,hourly,alerts&appid=8ec6e701e0931857229292171b235eaa")
    Call<Response> getWeathers(@Query("lat") double latitude, @Query("lon") double longitude);

    @GET("2.5/forecast?units=metric&appid=8ec6e701e0931857229292171b235eaa")
    Call<Response> getWeatherForecase(@Query("q") String cityLocation);
}
