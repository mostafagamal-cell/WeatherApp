package com.example.weatherapp.DataSource

import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.MyNetwork.Iweather
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import retrofit2.Response

class RemoteDataSource(val api: Iweather) {

     suspend fun getWeather(lat:Double,lon:Double,lang:String): Response<ExampleJson2KtKotlin> {
       return api.getWeatherByCity(lat,lon,lang)
    }
    suspend fun getForecast(lat:Double,lon:Double,lang:String): Response<Forcast> {
        return api.getForecastByCity(lat,lon,lang)
    }
}