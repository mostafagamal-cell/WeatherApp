package com.example.weatherapp

import android.app.Application
import android.util.Log
import com.example.weatherapp.MainActivity.Companion.allcities
import com.example.weatherapp.weathermodel.cityes
import com.google.gson.Gson
import org.json.JSONArray

class App:Application() {
    override fun onCreate() {
        super.onCreate()
                val inputStream = resources.openRawResource(R.raw.cities)
                val reader = inputStream.bufferedReader()
                 allcities = Gson().fromJson(reader.readText(), Array<cityes>::class.java)
                Log.e("allcities", allcities!!.size.toString())
                reader.close()
    }
}