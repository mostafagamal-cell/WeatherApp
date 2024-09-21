package com.example.weatherapp.MyNetwork

import com.example.weatherapp.ApiKey
import com.example.weatherapp.ForcastModel.Forcast
import com.example.weatherapp.WeatherModel.ExampleJson2KtKotlin
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val url = "https://api.openweathermap.org/data/2.5/"
val retro = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).build()
interface Iweather
{
    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String
        ,@Query("lang") lang: String
        ,@Query("apiKey") apiKey: String= ApiKey)
        : Response<ExampleJson2KtKotlin>
    @GET("forecast")
    suspend fun getForecastByCity(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String,
        @Query("appid") apiKey: String= ApiKey
    ): Response<Forcast>
}
val API by lazy {
    retro.create(Iweather::class.java)
}