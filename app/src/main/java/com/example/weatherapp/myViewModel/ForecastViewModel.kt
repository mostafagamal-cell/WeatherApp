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
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone

class ForecastViewModelFac(val localDataSource: LocalDataSource, val remoteDataSource: RemoteDataSource) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ForecastViewModel(Repo.getInstance(localDataSource,remoteDataSource)) as T
    }
}
class ForecastViewModel(val repo: Repo):ViewModel() {
    private val _state= MutableLiveData<States>()
    private val _forecast= MutableLiveData<Forcast>()
    val forecast:MutableLiveData<Forcast>
        get()=_forecast
    val state: LiveData<States>get() = _state
    private val _weather= MutableLiveData<ExampleJson2KtKotlin>()
    val weather:LiveData<ExampleJson2KtKotlin>
        get()=_weather
    fun getForecast(lat:Double,lon:Double,lang:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = States.Loading
              repo.getForecast(lat,lon, lang  ).collect {
                    _forecast.value = it
                    _state.value = States.Success
                }
            }catch (e:Exception){
                _state.value = States.Error
            }
        }
    }
    fun getWeather(lat:Double,lon:Double,lang:Int){
        _state.value = States.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                 repo.getWeather(lat,lon,lang).collect {
                     _weather.postValue( it)
                     Log.i("eeeeeeeeeeeeeeeee",it.toString())
                     _state.postValue (States.Success)
                }

            }catch (e:Exception){
                Log.i("eeeeeeeeeeeeeeeee",e.message.toString())
                _state.postValue (States.Error)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDailyForcast(lat:Double,lon:Double,lang:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = States.Loading
                repo.getDailyForecast(lat,lon,lang).collect { data ->
                    val timeZone = TimeZone.getTimeZone("GMT+${data.city.timezone?.div(3600)}")
                    val start = Calendar.getInstance()
                    start.timeZone = timeZone
                    start.set(Calendar.HOUR_OF_DAY, 0)
                    start.set(Calendar.MINUTE, 0)
                    start.set(Calendar.SECOND, 0)
                    start.set(Calendar.MILLISECOND, 0)
                    val end = Calendar.getInstance()
                    end.timeZone = timeZone
                    end.set(Calendar.HOUR_OF_DAY, 23)
                    end.set(Calendar.MINUTE, 59)
                    end.set(Calendar.SECOND, 59)
                    end.set(Calendar.MILLISECOND, 999)
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val startZonedDateTime = start.toInstant().atZone(timeZone.toZoneId())
                    val endZonedDateTime = end.toInstant().atZone(timeZone.toZoneId())
                    val x = formatter.format(startZonedDateTime)
                    val y = formatter.format(endZonedDateTime)
                    println(x)
                    println(y)
                    val list = ArrayList<com.example.weatherapp.forcastmodel.List>()
                    for (i in data.list) {
                        val e = i.dt!!
                        println("$e      $x    $y")
                        if (i.dtTxt in x..y) {
                            list.add(i)
                        }
                    }
                }
                _state.value = States.Success
            }catch (e:Exception){
                _state.value = States.Error
         }
        }
    }
}