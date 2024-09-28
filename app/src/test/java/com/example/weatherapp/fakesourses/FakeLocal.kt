package com.example.weatherapp.fakesourses

import com.example.weatherapp.DataSource.ILocalDataSource
import com.example.weatherapp.alerts.MyAlerts
import com.example.weatherapp.forcastmodel.Favorites
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocal:ILocalDataSource {

    val weathers= arrayListOf<ExampleJson2KtKotlin>()
    val favourts= arrayListOf<Favorites>()
    var forcast : Forcast?=null
    val alerts= arrayListOf<MyAlerts>()
    override suspend fun insertForecast(forecast: Forcast) {
        forcast=forecast
    }

    override fun getForecast(lat: Double, lon: Double, lang: Int): Flow<Forcast?> {
        return flow {
            emit(forcast)}
    }

    override fun getFavorite(): Flow<List<Favorites>> {
        return flow {emit(favourts)}
    }

    override suspend fun insertWeather(weather: ExampleJson2KtKotlin): Long {
        weathers.add(weather)
       return weathers.indexOf(weather).toLong()
    }

    override fun getWeather(cityName: Int, e: Int): Flow<ExampleJson2KtKotlin?> {
        return flow {emit(weathers.find { it.language==cityName && it.id==e })}
    }

    override fun getWeather(lat: Double, lon: Double, lang: Int): Flow<ExampleJson2KtKotlin?> {
        println(weathers.size)
       return flow {
           emit(weathers.find { it.lat==lat && it.lon==lon && it.language==lang})
       }
    }

    override fun getAlerts(): Flow<List<MyAlerts>?> {
     return flow {emit(alerts)}
    }

    override suspend fun addAlert(weather: MyAlerts): Long {
        alerts.add(weather)
      return alerts.indexOf(weather).toLong()
    }

    override suspend fun deleteAlert(weather: MyAlerts) {
        alerts.remove(weather)
    }

    override fun getAlert(id: Int): Flow<MyAlerts?> {
       return flow {emit(alerts.find { it.id==id })}
    }

    override suspend fun addFavorite(name: Favorites) {
        favourts.add(name)
    }

    override suspend fun deleteFavorite(name: Favorites) {
        favourts.remove(name)
    }
}