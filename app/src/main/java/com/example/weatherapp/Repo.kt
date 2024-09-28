package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weatherapp.alerts.MyAlerts
import com.example.weatherapp.AppViews.consts
import com.example.weatherapp.DataSource.ILocalDataSource
import com.example.weatherapp.DataSource.IRemoteDataSource
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
    private val localDataSource: ILocalDataSource
    , private val remoteDataSource: IRemoteDataSource) : IRepo {
    companion object {
        private var instance: IRepo? = null
        fun getInstance(localDataSource: ILocalDataSource, remoteDataSource: IRemoteDataSource): IRepo {
            return instance ?: Repo(localDataSource, remoteDataSource)
        }
    }

      @SuppressLint("SuspiciousIndentation")
      override fun getWeather(lat:Double, lon:Double, lang:Int): Flow<ExampleJson2KtKotlin?> = runBlocking {
          val m=consts.en.ordinal
          val m2=consts.ar.ordinal
        launch {
            remoteDataSource.getWeather(lat, lon, "en").catch() {
                Log.i("dddddddddddddddddddddddd", "${it.message}")
            }.collect {
                Log.i("dddddddddddddddddddddddd", "eeeeeeeeeeeeeeeeeee")
                if(it!=null) {
                    if (it.isSuccessful) {
                        Log.i("dddddddddddddddddddddddd", "${it.body()}")
                        it.body()?.lat = lat
                        it.body()?.lon = lon
                        it.body()?.language = m
                        localDataSource.insertWeather(it.body()!!)
                    }
                }
                cancel()
            }
        }
          launch {
              remoteDataSource.getWeather(lat, lon, "ar").catch {
                  Log.i("dddddddddddddddddddddddd", "${it.message}")
              }.collect {
                  Log.i("dddddddddddddddddddddddd", "eeeeeeeeeeeeeeeeeee")
                  if(it!=null) {
                      if (it.isSuccessful) {
                          Log.i("dddddddddddddddddddddddd", "eeeeeeeeeeeeeeeeeee")
                          it.body()?.lat = lat
                          it.body()?.lon = lon
                          it.body()?.language = m2
                          localDataSource.insertWeather(it.body()!!)
                      }
                  }
                  cancel()
              }
          }
          return@runBlocking localDataSource.getWeather(lat,lon,lang)
    }
         override fun getForecast(lat:Double, lon:Double, lang:Int):Flow<Forcast?> = runBlocking{
          launch {
                remoteDataSource.getForecast(lat, lon, "en").catch {
                    Log.i("dddddddddddddddddddddddd", "${it.message}")
                }.collect {
                    if(it!=null) {
                    if (it.isSuccessful) {
                        it.body()?.lang = consts.en.ordinal
                        it.body()?.cityName = it.body()?.city?.name.toString()
                        it.body()?.lat = lat
                        it.body()?.lon = lon
                        if (it.body() != null && it.body()?.list != null && it.body()?.list?.size != 0) {
                            localDataSource.insertForecast(it.body()!!)
                        }
                    }
                    }
                    cancel()
                }
            }
              launch { remoteDataSource.getForecast(lat, lon, "ar").catch {
                  Log.i("dddddddddddddddddddddddd", "${it.message}")
              }.collect {
                  if(it!=null) {
                    if (it.isSuccessful) {
                        it.body()?.lat = lat
                        it.body()?.lon = lon
                        it.body()?.lang = consts.ar.ordinal
                        it.body()?.cityName = it.body()?.city?.name.toString()
                        if (it.body() != null && it.body()?.list != null && it.body()?.list?.size != 0) {
                            localDataSource.insertForecast(it.body()!!)
                        }
                        }
                    }
                cancel()
                }
            }

             return@runBlocking localDataSource.getForecast(lat,lon,lang)
    }

     override fun getFavorite():Flow<List<Favorites>?>{
        return localDataSource.getFavorite()
    }
    override suspend fun addFavorite(name: Favorites){
        localDataSource.addFavorite(name)
    }
    override suspend fun deleteFavorite(name:Favorites){
        localDataSource.deleteFavorite(name)
    }

    override suspend fun addAlert(myAlerts: MyAlerts):Long{
       return localDataSource.addAlert(myAlerts)
    }
    override suspend fun deleteAlert(myAlerts: MyAlerts){
        localDataSource.deleteAlert(myAlerts)
    }
     override fun getAlerts():Flow<List<MyAlerts>?>{
        return localDataSource.getAlerts()
    }
    override  fun getAlert(id:Int):Flow<MyAlerts?>{
        return localDataSource.getAlert(id)
    }

}