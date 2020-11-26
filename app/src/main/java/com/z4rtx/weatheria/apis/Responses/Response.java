package com.z4rtx.weatheria.apis.Responses;

import java.util.List;

public class Response {
    //List of

    private
    List<Forecast> list;

    //City
    private
    City city;

    public List<Forecast> getList() {
        return list;
    }

    public City getCity() {
        return city;
    }
}
