package com.example.weatherapp.DataSource

import com.example.weatherapp.Alerts.MyAlerts
import com.example.weatherapp.ForcastModel.Forcast
import com.example.weatherapp.ForecastDatabase.ForecastDao
import com.example.weatherapp.WeatherModel.ExampleJson2KtKotlin

class LocalDataSource(val dataBase: ForecastDao) {
   suspend fun insertForecast(forecast: Forcast) {
        return dataBase.insert(forecast)
    }
    suspend fun getForecast(cityName: String): Forcast {
        return dataBase.getAllForecast(cityName)
    }
    suspend fun deleteForecast(forecast: Forcast) {
        return dataBase.deleteAll(forecast)
    }
    suspend fun getFavorite(): List<ExampleJson2KtKotlin> {
        return dataBase.getfavorite()
    }
    suspend fun insertWeather(weather: ExampleJson2KtKotlin) {
        return dataBase.insert(weather)
    }
    suspend fun getWeather(cityName: String): ExampleJson2KtKotlin {
        return dataBase.getWeather(cityName)
    }
    suspend fun deleteWeather(weather: ExampleJson2KtKotlin) {
        return dataBase.deleteAll(weather)
    }
    suspend fun getAlerts(): List<MyAlerts> {
        return dataBase.getAlerts()
    }
    suspend fun addAlert(weather: MyAlerts) {
        return dataBase.addAlerm(weather)
    }
    suspend fun deleteAlert(weather: MyAlerts) {
        return dataBase.deleteAlert(weather)
    }
    suspend fun getAlert(id:Int): MyAlerts {
        return dataBase.getAlert(id)
    }
    suspend fun addFavorite(name: String) {
        return dataBase.addFavorite(name)
    }
    suspend fun deleteFavorite(name: String) {
        return dataBase.deleteFavorite(name)}

    suspend fun getAllWeather(): List<ExampleJson2KtKotlin> {
        return dataBase.getAllWeather()
    }
    suspend fun addWeather(weather: ExampleJson2KtKotlin) {
        dataBase.insert(weather)
        }
}