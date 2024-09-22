package com.example.weatherapp.myViewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.Repo
import com.example.weatherapp.States
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
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

    private val _forecast = MutableStateFlow<State>(State.Empty)
    val forecast: StateFlow<State> get() = _forecast

    private val _weather = MutableStateFlow<State>(State.Empty)
    val weather: StateFlow<State> get() = _weather

    fun getWeather(lat: Double, lon: Double, lang: Int) {
        weatherJob?.cancel()  // Cancel the previous weather request if running
        _weather.value = State.Empty  // Reset the state to avoid old data
        weatherJob = viewModelScope.launch(Dispatchers.IO) {
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

    fun getForecast(lat: Double, lon: Double, lang: Int) {
        forecastJob?.cancel()  // Cancel previous forecast request if running
        _forecast.value = State.Empty  // Reset the state
        forecastJob = viewModelScope.launch(Dispatchers.IO) {
            _forecast.value = State.Loading
            try {
                repo.getForecast(lat, lon, lang)
                    .catch { e -> _forecast.value = State.Error(e) }
                    .collect { data -> _forecast.value = State.Success(data) }
            } catch (e: Exception) {
                _forecast.value = State.Error(e)
            }
        }
    }
}

sealed class State{
    class Success(val data:Any):State()
    class Error(val message:Throwable):State()
    object Loading:State()
    object Empty:State()
}