package com.example.weatherapp.DataSource

import com.example.weatherapp.ForcastModel.Forcast
import com.example.weatherapp.MyNetwork.Iweather
import com.example.weatherapp.WeatherModel.ExampleJson2KtKotlin
import okhttp3.ResponseBody
import retrofit2.Response

class RemoteDataSource(val api: Iweather) {

     fun getWeather(city:String,lang:String): Response<ExampleJson2KtKotlin> {
       return api.getWeatherByCity(city,lang).execute()
    }
     fun getForecast(cityName:String,lang:String): Response<Forcast> {
        return api.getForecastByCity(cityName,lang).execute()
    }
}