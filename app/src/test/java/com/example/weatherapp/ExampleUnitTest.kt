package com.example.weatherapp

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.AppViews.consts
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.MyNetwork.API
import com.example.weatherapp.WeatherModel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RunWith(AndroidJUnit4::class)
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        val d = Instant.now() // Current UTC time
        val e = API.getWeatherByCity("Moscow", ApiKey).execute()

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
    fun testGetWeather() = runTest {
        // Set up context and database
        val context = ApplicationProvider.getApplicationContext<Context>() as Application
        val db = ForecastDataBase.getDatabase(context)

        // Set the shared preference for language
        context.getSharedPreferences(settings, Context.MODE_PRIVATE).edit()
            .putInt(language, consts.ar.ordinal).apply()

        // Create the repository instance with the local and remote data sources
        val repo = Repo.getInstance(LocalDataSource(db.yourDao()), RemoteDataSource(API))

        // Collect the weather data for Cairo and assert
        val weatherFlow = repo.getWeather("القاهرة", consts.ar.ordinal)
        var x: ExampleJson2KtKotlin? = null
        weatherFlow.collect { e ->
            x = e
            println("Collected weather data: $e")
            assertEquals("القاهرة", x?.name)
        }
        println("Final weather data: $x")
        assertEquals("القاهرة", x?.name)
    }

}