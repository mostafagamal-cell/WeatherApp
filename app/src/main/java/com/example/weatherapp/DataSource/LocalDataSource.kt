package com.example.weatherapp.DataSource

import com.example.weatherapp.alerts.MyAlerts
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.ForecastDatabase.ForecastDao
import com.example.weatherapp.forcastmodel.Favorites
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val dataBase: ForecastDao) : ILocalDataSource {
   override suspend fun insertForecast(forecast: Forcast) {
        return dataBase.insert(forecast)
    }
     override fun getForecast(lat:Double, lon:Double, lang:Int): Flow<Forcast?> {
        return dataBase.getAllForecast(lat,lon,lang)
    }
     override fun getFavorite(): Flow<List<Favorites>?> {
        return dataBase.getfavorite()
    }
    override suspend fun insertWeather(weather: ExampleJson2KtKotlin):Long {
        return dataBase.insert(weather)
    }
     override fun getWeather(cityName: Int, e:Int): Flow<ExampleJson2KtKotlin?> {
        return dataBase.getWeather(cityName,e)
    }
    override fun getWeather(lat:Double, lon:Double, lang:Int): Flow<ExampleJson2KtKotlin?> {
        return dataBase.getWeather(lat,lon,lang)
    }

     override fun getAlerts(): Flow<List<MyAlerts>?> {
        return dataBase.getAlerts()
    }
    override suspend fun addAlert(weather: MyAlerts):Long {
        return dataBase.addAlerm(weather)
    }
    override suspend fun deleteAlert(weather: MyAlerts) {
        return dataBase.deleteAlert(weather)
    }
     override fun getAlert(id:Int): Flow<MyAlerts> {
        return dataBase.getAlert(id)
    }
    override suspend fun addFavorite(name: Favorites) {
        return dataBase.addFavorite(name)
    }
    override suspend fun deleteFavorite(name: Favorites) {
        return dataBase.deleteFavorite(name)}
}