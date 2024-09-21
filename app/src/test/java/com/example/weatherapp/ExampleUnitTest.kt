package com.example.weatherapp

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.loader.ResourcesProvider
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.Alerts.MyAlerts
import com.example.weatherapp.AppViews.consts
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.MyBrodcasts.AlertsBrodcast
import com.example.weatherapp.MyNetwork.API
import com.example.weatherapp.WeatherModel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.internal.bytecode.ResourceProvider
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar

@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {

    @Test
    fun addition_isCorrect()= runBlocking{
        val d = Instant.now() // Current UTC time
        val e = API.getWeatherByCity("Moscow", ApiKey)

        val timezoneOffsetInSeconds = e.body()?.timezone ?: 0
        val adjustedTime = d.plusSeconds(timezoneOffsetInSeconds.toLong())
        val nd = LocalDateTime.ofInstant(
            adjustedTime,
            ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(timezoneOffsetInSeconds))
        )

        println(nd.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        try {
            println(e.body())
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    @Test
    fun testFlowConsumer() = runTest {
        val flow = flowOf(1, 2, 3)
        val result = mutableListOf<Int>()
        flow.collect { result.add(it) }
        assertEquals(listOf(1, 2, 3), result)
    }

    @Test
    fun testGetWeather() = runBlocking {
        val context = ApplicationProvider.getApplicationContext() as Application
        val db = ForecastDataBase.getDatabase(context)
        context.getSharedPreferences(settings, Context.MODE_PRIVATE).edit()
            .putInt(language, consts.ar.ordinal).apply()

        val repo = Repo.getInstance(LocalDataSource(db.yourDao()), RemoteDataSource(API))

        val alert=MyAlerts(1,1,Calendar.getInstance().timeInMillis,Calendar.getInstance().timeInMillis+1000000,"Cairo")
        val e= repo.addAlert(alert)
        val oo=repo.getAlerts()
         println("size :    "+oo)
        var x: ExampleJson2KtKotlin? = null
        val intent= Intent(context,AlertsBrodcast::class.java)
        intent.putExtra("id",1)
        AlertsBrodcast().onReceive(context, intent)
    }
    @Test
    fun getForecast() = runBlocking {
        val context = ApplicationProvider.getApplicationContext() as Application
        val db = ForecastDataBase.getDatabase(context)
        val repo = Repo.getInstance(LocalDataSource(db.yourDao()), RemoteDataSource(API))

        val data=repo.getDailyForecast(30.0081,31.2357)
       data.forEach {
           println(it)
       }
        assertNotEquals(0,data.size)
    }

}