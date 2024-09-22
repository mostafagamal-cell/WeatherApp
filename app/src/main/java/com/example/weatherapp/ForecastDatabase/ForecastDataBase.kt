package com.example.weatherapp.ForecastDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.Alerts.MyAlerts
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin

@Database(entities = [Forcast::class,ExampleJson2KtKotlin::class, MyAlerts::class], version = 1)
@TypeConverters(ForecastTypeConverter::class, WeatherTypeConverter::class)
abstract class ForecastDataBase : RoomDatabase(){
    abstract fun yourDao(): ForecastDao
    companion object {
        @Volatile
        private var INSTANCE: ForecastDataBase? = null
        fun getDatabase(context: Context): ForecastDataBase {
            return  INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ForecastDataBase::class.java,
                    "ForecastDB"
                ).build()
                INSTANCE = instance
                instance
                 }
            }
            }
        }

