package com.example.weatherapp.MyNetwork

import com.example.weatherapp.ApiKey
import com.example.weatherapp.ForcastModel.Forcast
import com.example.weatherapp.WeatherModel.ExampleJson2KtKotlin
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val url = "https://api.openweathermap.org/data/2.5/"
val retro = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).build()
interface Iweather
{
    @GET("weather")
    fun getWeatherByCity(@Query("q") city: String, @Query("apiKey") apiKey: String= ApiKey): Call<ExampleJson2KtKotlin>
    @GET("forecast")
    fun getForecastByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String= ApiKey
    ): Call<Forcast>
}
val API by lazy {
    retro.create(Iweather::class.java)
}