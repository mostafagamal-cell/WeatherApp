package com.example.weatherapp

import com.example.weatherapp.DataSource.ILocalDataSource
import com.example.weatherapp.DataSource.IRemoteDataSource
import com.example.weatherapp.alerts.MyAlerts
import com.example.weatherapp.forcastmodel.Favorites
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepo private constructor(
    private val localDataSource: ILocalDataSource
    , private val remoteDataSource: IRemoteDataSource
) : IRepo {
    companion object {
        private var instance: FakeRepo? = null
        fun getInstance(localDataSource: ILocalDataSource, remoteDataSource: IRemoteDataSource): FakeRepo {
            return instance ?: FakeRepo(localDataSource, remoteDataSource)
        }
    }
    override fun getWeather(lat: Double, lon: Double, lang: Int): Flow<ExampleJson2KtKotlin?> {
       return localDataSource.getWeather(lat,lon,lang)
    }

    override fun getForecast(lat: Double, lon: Double, lang: Int): Flow<Forcast?> {
        return localDataSource.getForecast(lat,lon,lang)
    }

    override fun getFavorite(): Flow<List<Favorites>?> {
        return localDataSource.getFavorite()
    }

    override suspend fun addFavorite(name: Favorites) {
        localDataSource.addFavorite(name)
    }

    override suspend fun deleteFavorite(name: Favorites) {
       localDataSource.deleteFavorite(name)
    }

    override suspend fun addAlert(myAlerts: MyAlerts): Long {
        localDataSource.addAlert(myAlerts)
        return alerts.indexOf(myAlerts).toLong()
    }

    override suspend fun deleteAlert(myAlerts: MyAlerts) {
        localDataSource.deleteAlert(myAlerts)
    }

    override fun getAlerts(): Flow<List<MyAlerts>?> {
        return localDataSource.getAlerts()
    }

    override fun getAlert(id: Int): Flow<MyAlerts?> {
        return localDataSource.getAlert(id)
    }
    suspend fun addForcast(forcast: Forcast){
        localDataSource.insertForecast(forcast)
    }
    suspend fun addWeather(weather: ExampleJson2KtKotlin){
        localDataSource.insertWeather(weather)

    }
}