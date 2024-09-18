package com.example.weatherapp

import com.example.weatherapp.Alerts.MyAlerts
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.ForcastModel.Forcast
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.WeatherModel.ExampleJson2KtKotlin
import java.io.IOError

class Repo(val localDataSource: LocalDataSource
,val remoteDataSource: RemoteDataSource) {
    suspend fun getWeather(city:String): ExampleJson2KtKotlin {
        try {
             val e = remoteDataSource.getWeather(city,"en")
             val d=  remoteDataSource.getWeather(city,"ar")
             localDataSource.insertWeather(e.body()!!)
             localDataSource.insertWeather(d.body()!!)
             return localDataSource.getWeather(city)
        }catch (e:IOError){
            throw e
        }
    }
    suspend fun getForecast(cityName:String):Forcast{
        try {
            val e = remoteDataSource.getForecast(cityName,"en")
            val d=  remoteDataSource.getForecast(cityName,"ar")
            e.body()?.lang="en"
            d.body()?.lang="ar"
            localDataSource.insertForecast(d.body()!!)
            localDataSource.insertForecast(e.body()!!)
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
    suspend fun addAlert(myAlerts: MyAlerts){
        localDataSource.addAlert(myAlerts)
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