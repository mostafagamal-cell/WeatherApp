package com.example.weatherapp.myViewModel


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.Repo
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.forcastmodel.List
import com.example.weatherapp.getDayHourFromTimestamp
import com.example.weatherapp.getDayNameFromTimestamp
import com.example.weatherapp.getTimeZoneFromOffset
import com.example.weatherapp.getTimeZoneFromOffset2
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone

class ForecastViewModelFac(val localDataSource: LocalDataSource, val remoteDataSource: RemoteDataSource) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ForecastViewModel(Repo.getInstance(localDataSource,remoteDataSource)) as T
    }
}class ForecastViewModel(val repo: Repo) : ViewModel() {
    private var weatherJob: Job? = null
    private var forecastJob: Job? = null

    private val _forecast = MutableStateFlow<State>(State.Loading)
    val forecast: StateFlow<State> get() = _forecast
    private val _day = MutableStateFlow<State>(State.Loading)
    val day: StateFlow<State> get() = _day
    private val _hours = MutableStateFlow<State>(State.Loading)
    val hours: StateFlow<State> get() = _hours
    private val _weather = MutableStateFlow<State>(State.Loading)
    val weather: StateFlow<State> get() = _weather

    fun getWeather(lat: Double, lon: Double, lang: Int) {
        weatherJob?.cancel()  // Cancel the previous weather request if running
        _weather.value = State.Loading  // Reset the state to avoid old data
        weatherJob = viewModelScope.launch {
            _weather.value = State.Loading
                repo.getWeather(lat, lon, lang)
                    .catch { e -> _weather.value = State.Error(e) }
                    .collect { data ->
                        if (data != null) {
                            _weather.value = State.Success(data)
                        }else{
                            _weather.value=State.Error(Exception("no data found"))
                        }
                    }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getForecasts(lat: Double, lon: Double, lang: Int) {
        forecastJob?.cancel()  // Cancel previous forecast request if running
        _forecast.value = State.Loading  // Reset the state
        forecastJob = viewModelScope.launch{
            _forecast.value = State.Loading
                repo.getForecast(lat, lon, lang)
                    .collect { data ->
                        if (data != null) {
                            _forecast.value = State.Success(data)
                            getTodayForecast(data, lang)
                            getWeekForecast(data, lang)
                        } else{
                            _forecast.value=State.Error(Exception("no data found"))
                }
           }

        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodayForecast(data: Forcast, lang: Int) {
        _hours.value = State.Loading



        val  mytimezone=TimeZone.getTimeZone(getTimeZoneFromOffset2(data.city.timezone!!))

        val start = Calendar.getInstance(mytimezone)
        start.set(Calendar.HOUR_OF_DAY, 0)
        start.set(Calendar.MINUTE, 0)
        start.set(Calendar.SECOND, 0)
        start.set(Calendar.MILLISECOND, 0)

        val end = Calendar.getInstance(mytimezone)


        end.set(Calendar.HOUR_OF_DAY, 23)
        end.set(Calendar.MINUTE, 59)
        end.set(Calendar.SECOND, 59)
        end.set(Calendar.MILLISECOND, 999)

        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
            timeZone = mytimezone

        }

        Log.i("ForecastLog", "Success ${format.format(start.time)} ${format.format(end.time.time)}")

        // Filter the forecast list based on the start and end times
        val lists = data.list.mapNotNull { e ->
            val forecastTimeMillis = e.dt!! * 1000
            if (e.dtTxt.toString() in format.format(start.time)..format.format(end.time)) {
                e.time = getDayHourFromTimestamp(e.dtTxt!!.split(" ")[1], lang)
                e // Keep the forecast item
            } else {
                null // Exclude this forecast item
            }
        }

        Log.i("ForecastLog", "Filtered forecasts: ${lists}")

        // Update the forecast object
        val forecast = _forecast.value as State.Success
        val updatedForecast = forecast.data as Forcast
        updatedForecast.list = ArrayList(lists) // Update with the filtered list

        // Update the state to reflect success
        _hours.value = State.Success(updatedForecast)
    }

    fun getWeekForecast(it:Forcast,lang: Int) {
                   _day.value = State.Loading
                    val lists=it.list.map { e->
                        if (e.dtTxt!!.split(" ")[1]== "21:00:00"){
                            e.dayname=getDayNameFromTimestamp(e.dt!!,lang)
                            return@map e as List
                        } else {
                            return@map null
                        }
                    }
                    _day.value=State.Success(lists)
    }
}




sealed class State{
    class Success(val data:Any):State()
    class Error(val message:Throwable):State()
    data object Loading:State()
}