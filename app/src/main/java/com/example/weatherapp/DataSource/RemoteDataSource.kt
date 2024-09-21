package com.example.weatherapp.DataSource

import com.example.weatherapp.ForcastModel.Forcast
import com.example.weatherapp.MyNetwork.Iweather
import com.example.weatherapp.WeatherModel.ExampleJson2KtKotlin
import okhttp3.ResponseBody
import retrofit2.Response

class RemoteDataSource(val api: Iweather) {

     suspend fun getWeather(city:String,lang:String): Response<ExampleJson2KtKotlin> {
       return api.getWeatherByCity(city,lang)
    }
    suspend fun getForecast(lat:Double,lon:Double,lang:String): Response<Forcast> {
        return api.getForecastByCity(lat,lon,lang)
    }
}