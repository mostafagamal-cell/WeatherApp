package com.example.weatherapp.MyNetwork

import com.example.weatherapp.ApiKey
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val url = "https://api.openweathermap.org/data/2.5/"

val retro = Retrofit.Builder().baseUrl(url).build()

interface Iweather
{
    @GET("weather")
    fun getWeatherByCity(@Query("q") city: String, @Query("apiKey") apiKey: String= ApiKey): Call<ResponseBody>
    @GET("forecast")
    fun getForecastByCity(
        @Query("id") cityId: Int,
        @Query("appid") apiKey: String= ApiKey
    ): Call<ResponseBody>
}
val API by lazy {
    retro.create(Iweather::class.java)
}