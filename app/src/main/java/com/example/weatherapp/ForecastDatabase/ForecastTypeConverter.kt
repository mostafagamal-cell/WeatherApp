package com.example.weatherapp.ForecastDatabase
import androidx.room.TypeConverter

import com.example.weatherapp.forcastmodel.*
import com.example.weatherapp.forcastmodel.List
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class ForecastTypeConverter {
    private val gson = Gson()

    @TypeConverter
        fun fromCity(city: City?): String? {
            return Gson().toJson(city)
        }

        @TypeConverter
        fun toCity(cityString: String?): City? {
            return Gson().fromJson(cityString, City::class.java)
        }

        @TypeConverter
        fun fromClouds(clouds: Clouds?): String? {
            return Gson().toJson(clouds)
        }

        @TypeConverter
        fun toClouds(cloudsString: String?): Clouds? {
            return Gson().fromJson(cloudsString, Clouds::class.java)
        }

        @TypeConverter
        fun fromCoord(coord: Coord?): String? {
            return Gson().toJson(coord)
        }

        @TypeConverter
        fun toCoord(coordString: String?): Coord? {
            return Gson().fromJson(coordString, Coord::class.java)
        }

        @TypeConverter
        fun fromForcast(forcast: Forcast?): String? {
            return Gson().toJson(forcast)
        }

        @TypeConverter
        fun toForcast(forcastString: String?): Forcast? {
            return Gson().fromJson(forcastString, Forcast::class.java)
        }

        @TypeConverter
        fun fromList(list: List?): String? {
            return Gson().toJson(list)
        }

        @TypeConverter
        fun toList(listString: String?): List? {
            return Gson().fromJson(listString, List::class.java)
        }

        @TypeConverter
        fun fromMain(main: Main?): String? {
            return Gson().toJson(main)
        }

        @TypeConverter
        fun toMain(mainString: String?): Main? {
            return Gson().fromJson(mainString, Main::class.java)
        }

        @TypeConverter
        fun fromSys(sys: Sys?): String? {
            return Gson().toJson(sys)
        }

        @TypeConverter
        fun toSys(sysString: String?): Sys? {
            return Gson().fromJson(sysString, Sys::class.java)
        }

        @TypeConverter
        fun fromWeather(weather: ArrayList<Weather>?): String? {
            return Gson().toJson(weather)
        }

        @TypeConverter
        fun toWeather(weatherString: String?): ArrayList<Weather>? {
            val type = object : TypeToken<ArrayList<Weather>>() {}.type
            return Gson().fromJson(weatherString, type)
        }

        @TypeConverter
        fun fromWind(wind: Wind?): String? {
            return Gson().toJson(wind)
        }

        @TypeConverter
        fun toWind(windString: String?): Wind? {
            return Gson().fromJson(windString, Wind::class.java)
        }
    @TypeConverter
    fun fromListArray(list: ArrayList<List>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toListArray(listString: String): ArrayList<List> {
        val listType = object : TypeToken<ArrayList<List>>() {}.type
        return gson.fromJson(listString, listType)
    }
}