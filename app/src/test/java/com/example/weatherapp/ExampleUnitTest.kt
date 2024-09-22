package com.example.weatherapp

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.example.weatherapp.Alerts.MyAlerts
import com.example.weatherapp.AppViews.consts
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.MyBrodcasts.AlertsBrodcast
import com.example.weatherapp.MyNetwork.API
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
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
        val e = API.getWeatherByCity(55.7558,37.6173, ApiKey)

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

        val alert=MyAlerts(1,1,Calendar.getInstance().timeInMillis,Calendar.getInstance().timeInMillis+1000000,55.7558,37.6173)
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
        context.getSharedPreferences(settings, Context.MODE_PRIVATE).edit()
            .putInt(language, consts.ar.ordinal).apply()
        val repo = Repo.getInstance(LocalDataSource(db.yourDao()), RemoteDataSource(API))
        repo.getForecast(55.7558,37.6173,consts.ar.ordinal).collect {
            println(it)
        }

    }

}