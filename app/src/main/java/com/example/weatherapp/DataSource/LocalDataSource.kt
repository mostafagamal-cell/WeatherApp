package com.example.weatherapp.DataSource

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherapp.Alerts.MyAlerts
import com.example.weatherapp.ForcastModel.Forcast
import com.example.weatherapp.ForecastDatabase.ForecastDao
import com.example.weatherapp.WeatherModel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.Flow
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone

class LocalDataSource(private val dataBase: ForecastDao) {
   suspend fun insertForecast(forecast: Forcast) {
        return dataBase.insert(forecast)
    }
    suspend fun getForecast(lat:Double,lon:Double): Forcast {
        return dataBase.getAllForecast(lat,lon)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun day_forcast(lat:Double,lon:Double): List<com.example.weatherapp.ForcastModel.List> {
        val data= dataBase.getAllForecast(lat,lon)
        val timeZone = TimeZone.getTimeZone("GMT+${data.city.timezone?.div(3600)}")
        val start= Calendar.getInstance()
        start.timeZone= timeZone
        start.set(Calendar.HOUR_OF_DAY,0)
        start.set(Calendar.MINUTE,0)
        start.set(Calendar.SECOND,0)
        start.set(Calendar.MILLISECOND,0)
        val end = Calendar.getInstance()
        end.timeZone= timeZone
        end.set(Calendar.HOUR_OF_DAY, 23)
        end.set(Calendar.MINUTE, 59)
        end.set(Calendar.SECOND, 59)
        end.set(Calendar.MILLISECOND, 999)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val startZonedDateTime = start.toInstant().atZone(timeZone.toZoneId())
        val endZonedDateTime = end.toInstant().atZone(timeZone.toZoneId())
        val x=formatter.format(startZonedDateTime)
        val y=formatter.format(endZonedDateTime)
        println(x)
        println(y)
        val list=ArrayList<com.example.weatherapp.ForcastModel.List>()
        for (i in data.list){
            val e=i.dt!!
            println("$e      $x    $y")
            if (i.dtTxt in x..y){
                list.add(i)
            }
        }
        return list
    }
    suspend fun deleteForecast(forecast: Forcast) {
        return dataBase.deleteAll(forecast)
    }
    suspend fun getFavorite(): List<ExampleJson2KtKotlin> {
        return dataBase.getfavorite()
    }
    suspend fun insertWeather(weather: ExampleJson2KtKotlin):Long {
        return dataBase.insert(weather)
    }
     fun getWeather(cityName: Int,e:Int): Flow<ExampleJson2KtKotlin> {
        return dataBase.getWeather(cityName,e)
    }
    fun getWeather(cityName: String): Flow<ExampleJson2KtKotlin> {
        return dataBase.getWeather(cityName)
    }
    suspend fun deleteWeather(weather: ExampleJson2KtKotlin) {
        return dataBase.deleteAll(weather)
    }
    suspend fun getAlerts(): List<MyAlerts> {
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
    suspend fun addFavorite(name: String) {
        return dataBase.addFavorite(name)
    }
    suspend fun deleteFavorite(name: String) {
        return dataBase.deleteFavorite(name)}

    suspend fun getAllWeather(): List<ExampleJson2KtKotlin> {
        return dataBase.getAllWeather()
    }
    suspend fun addWeather(weather: ExampleJson2KtKotlin) {
        dataBase.insert(weather)
        }
}