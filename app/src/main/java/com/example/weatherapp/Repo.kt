package com.example.weatherapp

import com.example.weatherapp.Alerts.MyAlerts
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.ForcastModel.Forcast
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.WeatherModel.ExampleJson2KtKotlin
import java.io.IOError

class Repo(val localDataSource: LocalDataSource ,val remoteDataSource: RemoteDataSource) {
    suspend fun getWeather(city:String): ExampleJson2KtKotlin {
        try {
            val e = remoteDataSource.getWeather(city)
            if (e.code()==200) {
                localDataSource.insertWeather(e.body()!!)
            }else if (e.code()==401 ){
                throw Exception("Key is Invalid")
            }
        }catch (e:IOError){

        }
        return localDataSource.getWeather(city)
    }
    suspend fun getForecast(cityName:String):Forcast{
        try {
            val e = remoteDataSource.getForecast(cityName)
            if (e.isSuccessful) {
                localDataSource.insertForecast(e.body()!!)
            }else{
                throw Exception(e.message())
            }
        }catch (e:Exception){

        }
        return localDataSource.getForecast(cityName)
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
}