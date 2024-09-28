package com.example.weatherapp.fakesourses

import com.example.weatherapp.DataSource.IRemoteDataSource
import com.example.weatherapp.dumbData
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.myforecast
import com.example.weatherapp.weather
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeRemote:IRemoteDataSource {
    override fun getWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): Flow<Response<ExampleJson2KtKotlin>> {
        return flow {emit(Response.success(dumbData[0]))}
    }
    override fun getForecast(lat: Double, lon: Double, lang: String): Flow<Response<Forcast>> {
        return flow {emit(Response.success(myforecast))}}
}