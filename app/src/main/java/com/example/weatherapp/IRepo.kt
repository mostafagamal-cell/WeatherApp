package com.example.weatherapp

import android.annotation.SuppressLint
import androidx.annotation.RequiresApi
import com.example.weatherapp.alerts.MyAlerts
import com.example.weatherapp.forcastmodel.Favorites
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.Flow

interface IRepo {

    @SuppressLint("SuspiciousIndentation")
    fun getWeather(lat: Double, lon: Double, lang: Int): Flow<ExampleJson2KtKotlin?>
    fun getForecast(lat: Double, lon: Double, lang: Int): Flow<Forcast?>

    fun getFavorite(): Flow<List<Favorites>?>
    suspend fun addFavorite(name: Favorites)
    suspend fun deleteFavorite(name: Favorites)
    suspend fun addAlert(myAlerts: MyAlerts): Long
    suspend fun deleteAlert(myAlerts: MyAlerts)
    fun getAlerts(): Flow<List<MyAlerts>?>
     fun getAlert(id: Int): Flow<MyAlerts?>
}