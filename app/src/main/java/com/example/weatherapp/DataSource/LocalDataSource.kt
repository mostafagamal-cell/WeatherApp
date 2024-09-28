package com.example.weatherapp.DataSource

import com.example.weatherapp.alerts.MyAlerts
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.ForecastDatabase.ForecastDao
import com.example.weatherapp.forcastmodel.Favorites
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val dataBase: ForecastDao) {
   suspend fun insertForecast(forecast: Forcast) {
        return dataBase.insert(forecast)
    }
     fun getForecast(lat:Double,lon:Double,lang:Int): Flow<Forcast> {
        return dataBase.getAllForecast(lat,lon,lang)
    }
    suspend fun deleteForecast(forecast: Forcast) {
        return dataBase.deleteAll(forecast)
    }
     fun getFavorite(): Flow<List<Favorites>> {
        return dataBase.getfavorite()
    }
    suspend fun insertWeather(weather: ExampleJson2KtKotlin):Long {
        return dataBase.insert(weather)
    }
     fun getWeather(cityName: Int,e:Int): Flow<ExampleJson2KtKotlin> {
        return dataBase.getWeather(cityName,e)
    }
    fun getWeather(lat:Double,lon:Double,lang:Int): Flow<ExampleJson2KtKotlin> {
        return dataBase.getWeather(lat,lon,lang)
    }

    suspend fun deleteWeather(weather: ExampleJson2KtKotlin) {
        return dataBase.deleteAll(weather)
    }
     fun getAlerts(): Flow<List<MyAlerts>> {
        return dataBase.getAlerts()
    }
    suspend fun addAlert(weather: MyAlerts):Long {
        return dataBase.addAlerm(weather)
    }
    suspend fun deleteAlert(weather: MyAlerts) {
        return dataBase.deleteAlert(weather)
    }
    suspend fun getAlert(id:Int): MyAlerts {
        return dataBase.getAlert(id)
    }
    suspend fun addFavorite(name: Favorites) {
        return dataBase.addFavorite(name)
    }
    suspend fun deleteFavorite(name: Favorites) {
        return dataBase.deleteFavorite(name)}


    suspend fun addWeather(weather: ExampleJson2KtKotlin) {
        dataBase.insert(weather)
        }
    fun getTodayForecast(lat:Double,lon:Double,lang:Int): Flow<Forcast> {
        return dataBase.getTodayForecast(lat,lon,lang)

    }
}