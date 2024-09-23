package com.example.weatherapp

import android.app.Application
import android.util.Log
import com.example.weatherapp.weathermodel.cityes
import com.google.gson.Gson

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        if (getSharedPreferences(cities, MODE_PRIVATE).getString(cities, null) == null) {
                val inputStream = resources.openRawResource(R.raw.cities)
                val reader = inputStream.bufferedReader()
                val allCities = Gson().fromJson(reader.readText(), Array<cityes>::class.java)
                Log.e("allcities", allCities.size.toString())
                getSharedPreferences(cities, MODE_PRIVATE)
                    .edit()
                    .putString(cities, Gson().toJson(allCities))  // Convert back to JSON string
                    .apply()
                reader.close()
        }
        MainActivity.allcities = Gson().fromJson(getSharedPreferences(cities, MODE_PRIVATE).getString(cities, null), Array<cityes>::class.java)
        Log.e("allcities2",MainActivity.allcities!!.size.toString())

    }
}