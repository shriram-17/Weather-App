package com.bwap.weatherapp.WeatherApp.controller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WeatherServices {

    private OkHttpClient cilent;
    private Response response;
    private String CityName;
    String unit;

    public String getAPI() {
        return API;
    }

    private String API="9f9266eac7923fd27c9472d7d780102a";

    public org.json.JSONObject getWeather()
    {
        cilent=new OkHttpClient();
        Request request =new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q="+getCityName()+"&appid="+getAPI()+"&units="+getUnit())
                .build();
        try {
            response = cilent.newCall(request).execute();
            return new org.json.JSONObject(response.body().string());
        }
        catch (IOException e)

        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public JSONArray returnsWeatherArray() throws JSONException
    {
        JSONArray weatherArray=getWeather().getJSONArray("weather");
        return weatherArray;
    }

    public JSONObject returnMain() throws JSONException
    {
        JSONObject main=getWeather().getJSONObject("main");
        return main;
    }

    public JSONObject returnWind() throws JSONException
    {
        JSONObject wind=getWeather().getJSONObject("wind");
        return wind;
    }
    public JSONObject returnSys() throws JSONException
    {
        JSONObject sys=getWeather().getJSONObject("sys");
        return sys;
    }

}
