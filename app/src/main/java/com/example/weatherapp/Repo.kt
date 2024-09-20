package com.example.weatherapp

import com.example.weatherapp.Alerts.MyAlerts
import com.example.weatherapp.AppViews.consts
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.ForcastModel.Forcast
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.WeatherModel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import java.io.IOError
import java.io.IOException
import java.lang.NullPointerException
import java.net.UnknownHostException

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
       fun getWeather(city:String): Flow<ExampleJson2KtKotlin> = runBlocking{
        try {
             val e = remoteDataSource.getWeather(city,"en")
             val d=  remoteDataSource.getWeather(city,"ar")
             if (e.code()==404){
                 throw NullPointerException("no data found for that location")
             }
             if (e.code()==401){
                throw NullPointerException("key is not vaild")
             }
             val m=consts.en.ordinal
             val m2=consts.ar.ordinal
             e.body()?.language= m
             d.body()?.language= m2
             val y= localDataSource.insertWeather(e.body()!!)
             val x= localDataSource.insertWeather(d.body()!!)
            return@runBlocking localDataSource.getWeather(city)
        }catch (e: UnknownHostException){
            throw e
        }catch (e:IOException){
            throw e
        }
    }
    suspend fun getForecast(cityName:String):Forcast{
        try {
            val e = remoteDataSource.getForecast(cityName,"en")
            val d=  remoteDataSource.getForecast(cityName,"ar")
            if (e.isSuccessful){
                e.body()?.lang="en"
                e.body()?.cityName=cityName
                if (e.body()!=null&&e.body()?.list!=null && e.body()?.list?.size!=0){
                    localDataSource.insertForecast(e.body()!!)
                }
                localDataSource.insertForecast(e.body()!!)
            }
            if (d.isSuccessful) {
                d.body()?.lang = "ar"
                d.body()?.cityName = cityName
                if (d.body()!=null&&d.body()?.list!=null && d.body()?.list?.size!=0){
                    localDataSource.insertForecast(d.body()!!)
                }
            }
            return localDataSource.getForecast(cityName)
        }catch (e:IOError){
            throw e
        }

    }
    suspend fun getFavorite():List<ExampleJson2KtKotlin>{
        return localDataSource.getFavorite()
    }
    suspend fun addFavorite(name:String){
        localDataSource.addFavorite(name)
    }
    suspend fun deleteFavorite(name:String){
        localDataSource.deleteFavorite(name)
    }
    suspend fun getAllWeather():List<ExampleJson2KtKotlin>{
        return localDataSource.getAllWeather()
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
    suspend fun getAlerts():List<MyAlerts>{
        return localDataSource.getAlerts()
    }
    suspend fun getAlert(id:Int):MyAlerts{
        return localDataSource.getAlert(id)
    }
    suspend fun deleteForecast(forecast: Forcast){
        localDataSource.deleteForecast(forecast)
    }
    suspend fun addForecast(forecast: Forcast){
        localDataSource.insertForecast(forecast)
    }
    suspend fun getForecast(forecast: Forcast):Forcast{
        return localDataSource.getForecast(forecast.city.name)
    }

}