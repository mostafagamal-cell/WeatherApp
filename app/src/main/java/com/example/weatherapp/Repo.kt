package com.example.weatherapp

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weatherapp.Alerts.MyAlerts
import com.example.weatherapp.AppViews.consts
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.forcastmodel.Favorites
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import java.io.IOError
import java.io.IOException
import java.lang.NullPointerException
import java.net.UnknownHostException
import java.util.Calendar

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
      suspend fun getWeather(lat:Double, lon:Double, lang:Int): Flow<ExampleJson2KtKotlin> {
        try {
             val e = remoteDataSource.getWeather(lat,lon,"en")
             val d=  remoteDataSource.getWeather(lat,lon,"ar")
            val m=consts.en.ordinal
            val m2=consts.ar.ordinal
                if (e.body()!=null){
                    e.body()?.lat=lat
                    e.body()?.lon=lon
                    e.body()?.language= m
                    localDataSource.insertWeather(e.body()!!)
                    Log.i("eeeeeeeeeeeeeeeeeeeeeeeeeeee",e.body()!!.name)
                }
            if (e.body()!=null){
                d.body()?.lat=lat
                d.body()?.lon=lon
                d.body()?.language= m2
                localDataSource.insertWeather(d.body()!!)
                Log.i("eeeeeeeeeeeeeeeeeeeeeeeeeeee",d.body()!!.name)
            }
            return localDataSource.getWeather(lat,lon,lang)
        }catch (e: Exception){
            return localDataSource.getWeather(lat,lon,lang)
        }
    }
    suspend fun getForecast(lat:Double,lon:Double,lang:Int):Flow<Forcast>{
        try {
            val e = remoteDataSource.getForecast(lat,lon,"en")
            val d=  remoteDataSource.getForecast(lat,lon,"ar")
            if (e.isSuccessful){
                e.body()?.lang=consts.en.ordinal
                e.body()?.cityName= e.body()?.city?.name.toString()
                e.body()?.lat=lat
                e.body()?.lon=lon
                if (e.body()!=null&&e.body()?.list!=null && e.body()?.list?.size!=0){
                    localDataSource.insertForecast(e.body()!!)
                }
            }
            if (d.isSuccessful) {
                d.body()?.lat=lat
                d.body()?.lon=lon
                d.body()?.lang =consts.ar.ordinal
                d.body()?.cityName = d.body()?.city?.name.toString()
                if (d.body()!=null&&d.body()?.list!=null && d.body()?.list?.size!=0){
                    localDataSource.insertForecast(d.body()!!)
                }
            }
            return localDataSource.getForecast(lat,lon,lang)
        }catch (e:Exception){
            return localDataSource.getForecast(lat,lon,lang)
        }
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