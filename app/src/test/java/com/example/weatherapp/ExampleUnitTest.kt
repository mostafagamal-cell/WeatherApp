package com.example.weatherapp

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import com.example.weatherapp.AppViews.consts
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.MyNetwork.API
import com.example.weatherapp.alerts.MyAlerts
import com.example.weatherapp.fakesourses.FakeLocal
import com.example.weatherapp.fakesourses.FakeRemote
import com.example.weatherapp.forcastmodel.*
import com.example.weatherapp.myViewModel.ForecastViewModel
import com.example.weatherapp.myViewModel.State
import kotlinx.coroutines.cancel


import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RunWith(RobolectricTestRunner::class)
@LargeTest
class ExampleUnitTest {
    @Test
    fun testGetWeather(): Unit = runTest {
        val context = ApplicationProvider.getApplicationContext() as Application
        context.getSharedPreferences(settings, Context.MODE_PRIVATE).edit()
            .putInt(language, consts.en.ordinal).apply()
        val viewmodel= ForecastViewModel(repo!!)
        val lang=context.getSharedPreferences(settings, Context.MODE_PRIVATE).getInt(language,consts.en.ordinal)
        repo!!.addForcast(myforecast)
        viewmodel.getForecasts(52.12,13.4049,lang)
        launch {
            viewmodel.forecast.take(2).collect {
                if (it is State.Success) {
                    assertNotEquals(it.data, null)
                    println(it.data)
                    cancel()
                }
                if (it is State.Loading) {
                    assertEquals(State.Loading, it)
                }
                if (it is State.Error) {
                    println(it.message)
                    val e = it.message.message
                    assertEquals("no data found", e)
                    cancel()
                }
            }
        }
    }

    @Test
    fun testGetWeather2(): Unit = runTest {
        val context = ApplicationProvider.getApplicationContext() as Application
        context.getSharedPreferences(settings, Context.MODE_PRIVATE).edit()
            .putInt(language, consts.en.ordinal).apply()
        val viewmodel= ForecastViewModel(repo!!)
        val lang=context.getSharedPreferences(settings, Context.MODE_PRIVATE).getInt(language,consts.en.ordinal)
       // repo!!.addForcast(myforecast)
        viewmodel.getForecasts(524.12,13.4049,lang)
        launch {
        viewmodel.forecast.take(2).collect {
            if (it is State.Loading) {
                assertEquals(State.Loading, it)
            }
            if (it is State.Error) {
                println(it.message)
                val e = it.message.message
                assertEquals("no data found", e)
                cancel()
            }
        }
        }
    }
      var repo: FakeRepo?=null
        @Before
        fun setUp() {
            val context = ApplicationProvider.getApplicationContext() as Application
            context.getSharedPreferences(settings, Context.MODE_PRIVATE).edit()
                .putInt(language, consts.ar.ordinal).apply()
             repo=null
             repo = FakeRepo.getInstance(FakeLocal(), FakeRemote())
        }

    @Test
    fun test_add_and_get_weather_found(): Unit = runTest {
        for (i in dumbData){
            repo!!.addWeather(i)
        }
        val data= repo!!.getWeather(52.5200, 13.4049, consts.en.ordinal).firstOrNull()
        assertNotEquals(data,null)
    }
    @Test
    fun test_add_and_get_weather_failed(): Unit = runTest {
        for (i in dumbData){
            repo!!.addForcast(myforecast)
        }
        val data= repo!!.getWeather(52.12,13.4049, consts.en.ordinal).firstOrNull()
        assertEquals(data,null)
    }
    @Test
    fun test_add_and_get_forcast_found(): Unit = runTest {
        repo!!.addForcast(myforecast)
        val data= repo!!.getForecast(52.12,13.4049, consts.ar.ordinal).firstOrNull()
        assertNotEquals(data,null)
    }
    @Test
    fun test_add_and_get_Alert_found(): Unit = runTest {
        repo!!.addAlert(alerts[0])
        assertNotEquals(repo!!.getAlert(alerts[0].id).firstOrNull(),null)
    }
    @Test
    fun  test_add_and_get_Alert_fail(): Unit = runTest {
        repo!!.addAlert(alerts[0])
        assertEquals(repo!!.getAlert(alerts[1].id).firstOrNull(),null)
    }
    @Test
    fun test_add_alerm_found(): Unit = runTest {

        repo!!.addAlert(alerts[0])
        assertEquals(repo!!.getAlert(alerts[0].id).firstOrNull(),alerts[0])
    }
    @Test
    fun test_add_delete_alert(): Unit = runTest {
        println(repo!!.addAlert(alerts[0]))
        assertEquals(repo!!.getAlert(alerts[0].id).firstOrNull(),alerts[0])
        repo!!.deleteAlert(alerts[0])
        assertEquals(repo!!.getAlert(alerts[0].id).firstOrNull(),null)
    }
    @Test
    fun test_add_get_fav_found(): Unit = runTest {
        repo!!.addFavorite(favoriteLocations[0])
        assertNotEquals(repo!!.getFavorite().firstOrNull(),null)
    }
    @Test
    fun test_add_get_all_fav(): Unit = runTest {
        repo!!.addFavorite(favoriteLocations[0])
        assertEquals(repo!!.getFavorite().firstOrNull(), listOf(favoriteLocations[0]))
    }
    @Test
    fun test_add_delete_fav(): Unit = runTest {
        repo!!.addFavorite(favoriteLocations[0])
        assertEquals(repo!!.getFavorite().firstOrNull(), listOf(favoriteLocations[0]))
        repo!!.deleteFavorite(favoriteLocations[0])
        assertEquals(repo!!.getFavorite().firstOrNull(), emptyList<Favorites>())
    }
    @Test
    fun test_add_and_get_alerts_found(): Unit = runTest {
        val context = ApplicationProvider.getApplicationContext() as Application
        context.getSharedPreferences(settings, Context.MODE_PRIVATE).edit()
            .putInt(language, consts.en.ordinal).apply()
        val viewmodel= ForecastViewModel(repo!!)
        repo!!.addAlert(alerts[0])
        launch {
        viewmodel.alarm.take(2).collect{
            if (it is State.Success){
                assertNotEquals(it.data,null)
                println(it.data)
                cancel()
            }
            if (it is State.Loading){
                assertEquals(State.Loading,it)
            }
            if (it is State.Error){
                println(it.message)
                val e= it.message.message
                assertEquals("no data found",e)
                cancel()
            }
        }
        }
        viewmodel.getAlarms()
    }
    @Test
    fun test_add_and_get_alerts_empty(): Unit = runTest {
        val context = ApplicationProvider.getApplicationContext() as Application
        context.getSharedPreferences(settings, Context.MODE_PRIVATE).edit()
            .putInt(language, consts.en.ordinal).apply()
        val viewmodel= ForecastViewModel(repo!!)
      //  repo!!.addAlert(alerts[0])
        launch {
            viewmodel.alarm.take(2).collect{
                if (it is State.Success){
                    assertEquals((it.data as List<MyAlerts>).size,arrayListOf<MyAlerts>().size)
                    cancel()
                }
                if (it is State.Loading){
                    assertEquals(State.Loading,it)
                }
                if (it is State.Error){
                    println(it.message)
                    val e= it.message.message
                    assertEquals("no data found",e)
                    cancel()
                }
            }
        }
        viewmodel.getAlarms()
    }


    @After
    fun  tearDown(){
        repo=null
    }
}