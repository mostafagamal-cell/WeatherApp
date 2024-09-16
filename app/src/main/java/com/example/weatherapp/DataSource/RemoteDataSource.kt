package com.example.weatherapp.DataSource

import com.example.weatherapp.MyNetwork.Iweather
import okhttp3.ResponseBody
import retrofit2.Response

class RemoteDataSource(val api: Iweather) {

    fun getWeather(city:String): Response<ResponseBody> {
       return api.getWeatherByCity("cairo").execute()
    }
    fun getForecast(cityId:Int): Response<ResponseBody> {
        return api.getForecastByCity(cityId).execute()
    }
}