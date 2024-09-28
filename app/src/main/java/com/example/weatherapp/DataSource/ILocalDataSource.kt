package com.example.weatherapp.DataSource

import com.example.weatherapp.alerts.MyAlerts
import com.example.weatherapp.forcastmodel.Favorites
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {
    suspend fun insertForecast(forecast: Forcast)
    fun getForecast(lat: Double, lon: Double, lang: Int): Flow<Forcast?>
    fun getFavorite(): Flow<List<Favorites>?>

    suspend fun insertWeather(weather: ExampleJson2KtKotlin): Long
    fun getWeather(cityName: Int, e: Int): Flow<ExampleJson2KtKotlin?>
    fun getWeather(lat: Double, lon: Double, lang: Int): Flow<ExampleJson2KtKotlin?>
    fun getAlerts(): Flow<List<MyAlerts>?>

    suspend fun addAlert(weather: MyAlerts): Long

    suspend fun deleteAlert(weather: MyAlerts)
    fun getAlert(id: Int): Flow<MyAlerts?>

    suspend fun addFavorite(name: Favorites)

    suspend fun deleteFavorite(name: Favorites)
}