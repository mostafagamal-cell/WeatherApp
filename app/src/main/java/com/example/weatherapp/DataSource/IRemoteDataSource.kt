package com.example.weatherapp.DataSource

import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IRemoteDataSource {
    fun getWeather(lat: Double, lon: Double, lang: String): Flow<Response<ExampleJson2KtKotlin>?>
    fun getForecast(lat: Double, lon: Double, lang: String): Flow<Response<Forcast>?>
}