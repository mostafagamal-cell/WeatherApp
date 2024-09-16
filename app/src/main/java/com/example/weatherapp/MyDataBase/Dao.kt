package com.example.weatherapp.MyDataBase

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.ForcastModel.List
interface Dao {
    @Insert
    suspend fun insert(weather: List)
//    @Query("SELECT * FROM weather")
//    suspend fun getAll(): kotlin.collections.List<List>
    @Delete
    suspend fun deleteAll(weather: List)

}