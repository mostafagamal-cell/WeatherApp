package com.example.weatherapp.DataSource

import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.MyNetwork.Iweather
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class RemoteDataSource(val api: Iweather) {

      fun getWeather(lat:Double,lon:Double,lang:String): Flow<Response<ExampleJson2KtKotlin>> {
       return flow {
           emit(api.getWeatherByCity(lat,lon,lang))
       }
    }
     fun getForecast(lat:Double,lon:Double,lang:String): Flow<Response<Forcast>> {
        return flow {   emit(api.getForecastByCity(lat,lon,lang))}
    }
}