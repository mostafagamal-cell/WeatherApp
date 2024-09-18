package com.example.weatherapp

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.runner.AndroidJUnitRunner
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.MyNetwork.API

import org.junit.Test

import org.junit.Assert.*

import getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
 class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        val d = Instant.now() // Current UTC time
        val e = API.getWeatherByCity("Moscow", ApiKey).execute()

        val timezoneOffsetInSeconds = e.body()?.timezone ?: 0

        val adjustedTime = d.plusSeconds(timezoneOffsetInSeconds.toLong())

        val nd = LocalDateTime.ofInstant(adjustedTime, ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(timezoneOffsetInSeconds)))

        println(nd.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        try {
            println(e.body())
        }catch (e:Exception){
        println(e.message)
        }
        val x=0
    }
    @Test
     fun gettext()= runBlocking {
        val context= ApplicationProvider.getApplicationContext<Context>() as Application
        val db= ForecastDataBase.getDatabase(context)
        val repo=Repo(LocalDataSource(db.yourDao()), RemoteDataSource(API))
        repo.getWeather("Cairo")

        val data=  repo.getWeather("القاهرة")
        println(data)

    }
}