package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weatherapp.alerts.MyAlerts
import com.example.weatherapp.AppViews.consts
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.forcastmodel.Favorites
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Repo private constructor(
    private val localDataSource: LocalDataSource
    , private val remoteDataSource: RemoteDataSource) {
    companion object {
        private var instance: Repo? = null
        fun getInstance(localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource): Repo {
            return instance ?: Repo(localDataSource, remoteDataSource)
        }
    }
    fun getWeather(cityName: Int,lang:Int): Flow<ExampleJson2KtKotlin> {
        return localDataSource.getWeather(cityName,lang)
    }
      @SuppressLint("SuspiciousIndentation")
       fun getWeather(lat:Double, lon:Double, lang:Int): Flow<ExampleJson2KtKotlin> = runBlocking {
          val m=consts.en.ordinal
          val m2=consts.ar.ordinal
        launch {
            remoteDataSource.getWeather(lat, lon, "en").catch() {
                Log.i("dddddddddddddddddddddddd", "${it.message}")
            }.collect {
                Log.i("dddddddddddddddddddddddd", "eeeeeeeeeeeeeeeeeee")

                if (it.isSuccessful) {
                    Log.i("dddddddddddddddddddddddd", "${it.body()}")
                    it.body()?.lat = lat
                    it.body()?.lon = lon
                    it.body()?.language = m
                    localDataSource.insertWeather(it.body()!!)
                }
                cancel()
            }
        }
          launch {
              remoteDataSource.getWeather(lat, lon, "ar").catch {
                  Log.i("dddddddddddddddddddddddd", "${it.message}")
              }.collect {
                  Log.i("dddddddddddddddddddddddd", "eeeeeeeeeeeeeeeeeee")

                  if(it.isSuccessful){
                      Log.i("dddddddddddddddddddddddd", "eeeeeeeeeeeeeeeeeee")
                      it.body()?.lat = lat
                      it.body()?.lon = lon
                      it.body()?.language = m2
                      localDataSource.insertWeather(it.body()!!)
                  }
                  cancel()
              }
          }
          return@runBlocking localDataSource.getWeather(lat,lon,lang)
    }
         fun getForecast(lat:Double,lon:Double,lang:Int):Flow<Forcast> = runBlocking{

          launch {
                remoteDataSource.getForecast(lat, lon, "en").catch {
                    Log.i("dddddddddddddddddddddddd", "${it.message}")
                }.collect {
                    if (it.isSuccessful) {
                        it.body()?.lang = consts.en.ordinal
                        it.body()?.cityName = it.body()?.city?.name.toString()
                        it.body()?.lat = lat
                        it.body()?.lon = lon
                        if (it.body() != null && it.body()?.list != null && it.body()?.list?.size != 0) {
                            localDataSource.insertForecast(it.body()!!)
                        }

                    }
                    cancel()
                }
            }
              launch { remoteDataSource.getForecast(lat, lon, "ar").catch {
                  Log.i("dddddddddddddddddddddddd", "${it.message}")
              }.collect {
                    if (it.isSuccessful) {
                        it.body()?.lat = lat
                        it.body()?.lon = lon
                        it.body()?.lang = consts.ar.ordinal
                        it.body()?.cityName = it.body()?.city?.name.toString()
                        if (it.body() != null && it.body()?.list != null && it.body()?.list?.size != 0) {
                            localDataSource.insertForecast(it.body()!!)
                        }
                    }
                cancel()
                }
            }

             return@runBlocking localDataSource.getForecast(lat,lon,lang)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getDailyForecast(lat:Double,lon:Double,lang:Int):Flow<Forcast>{
       return getForecast(lat,lon,lang)
    }
     fun getFavorite():Flow<List<Favorites>>{
        return localDataSource.getFavorite()
    }
    suspend fun addFavorite(name: Favorites){
        localDataSource.addFavorite(name)
    }
    suspend fun deleteFavorite(name:Favorites){
        localDataSource.deleteFavorite(name)
    }
    suspend fun addWeather(weather: ExampleJson2KtKotlin){
        localDataSource.addWeather(weather)
    }
    suspend fun deleteWeather(weather: ExampleJson2KtKotlin){
        localDataSource.deleteWeather(weather)
    }
    suspend fun addAlert(myAlerts: MyAlerts):Long{
       return localDataSource.addAlert(myAlerts)
    }
    suspend fun deleteAlert(myAlerts: MyAlerts){
        localDataSource.deleteAlert(myAlerts)
    }
     fun getAlerts():Flow<List<MyAlerts>>{
        return localDataSource.getAlerts()
    }
    suspend fun getAlert(id:Int):MyAlerts{
        return localDataSource.getAlert(id)
    }

}