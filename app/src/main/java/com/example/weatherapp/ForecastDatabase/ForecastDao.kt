package com.example.weatherapp.ForecastDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.Alerts.MyAlerts
import com.example.weatherapp.forcastmodel.Favorites
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forecast: Forcast)
    @Query("SELECT * FROM forecast where  lat =:lat and lon=:lon and lang=:lang")
     fun getAllForecast(lat:Double,lon:Double,lang:Int): Flow<Forcast>
    @Query("SELECT * FROM forecast where cityName=:city")
    suspend fun getAllForecast(city:String): Forcast
    @Delete
    suspend fun deleteAll(forecast: Forcast)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite( name: Favorites)
    @Delete
    suspend fun deleteFavorite( name: Favorites)
    @Query("SELECT * FROM favorites")
    fun getfavorite(): Flow<List<Favorites>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: ExampleJson2KtKotlin):Long

    @Query("SELECT * FROM myweather where id=:myname and language=:lang")
    fun getWeather(myname: Int,lang:Int): Flow<ExampleJson2KtKotlin>

    @Query("SELECT * FROM myweather where lat=:lat and lon=:lon and language=:lang")
    fun getWeather(lat:Double,lon:Double,lang:Int): Flow<ExampleJson2KtKotlin>

    @Delete
    suspend fun deleteAll(weather: ExampleJson2KtKotlin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlerm(weather: MyAlerts):Long
    @Query("SELECT * FROM Alerts")
     fun getAlerts(): Flow<List<MyAlerts>>
    @Delete
    suspend fun deleteAlert(weather: MyAlerts)
    @Query("SELECT * FROM Alerts WHERE id=:id")
    suspend fun getAlert(id:Int): MyAlerts
    @Query("SELECT * FROM forecast where lat=:lat and lon=:lon and lang=:lang")
    fun getTodayForecast(lat:Double,lon:Double,lang:Int): Flow<Forcast>

}