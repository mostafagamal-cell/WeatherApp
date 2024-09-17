package com.example.weatherapp

import android.util.Log
import com.example.weatherapp.ForcastModel.Forcast
import com.example.weatherapp.MyNetwork.API
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.junit.Test

import org.junit.Assert.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter
import java.util.Date

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {


        val d = Instant.now() // Current UTC time
        val e = API.getWeatherByCity("Moscow", ApiKey).execute()

// Retrieve the timezone offset from the API response
        val timezoneOffsetInSeconds = e.body()?.timezone ?: 0

// Adjusting the time to the correct timezone
        val adjustedTime = d.plusSeconds(timezoneOffsetInSeconds.toLong())

// Convert the adjusted time to LocalDateTime
        val nd = LocalDateTime.ofInstant(adjustedTime, ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(timezoneOffsetInSeconds)))

// Format or use the LocalDateTime object as needed
        println(nd.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        try {
            println(e.body())
        }catch (e:Exception){
        println(e.message)
        }
        val x=0
    }
}