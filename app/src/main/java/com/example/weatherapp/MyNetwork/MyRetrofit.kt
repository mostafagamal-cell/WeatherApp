package com.example.weatherapp.MyNetwork

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
    fun getWeatherByCity(@Query("q") city: String, @Query("apiKey") apiKey: String): Call<ResponseBody>
    @GET("forecast")
    fun getWeatherByCityId(
        @Query("id") cityId: Int,
        @Query("appid") apiKey: String
    ): Call<ResponseBody>
}
val API by lazy {
    retro.create(Iweather::class.java)
}