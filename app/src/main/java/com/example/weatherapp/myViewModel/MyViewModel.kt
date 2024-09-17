package com.example.weatherapp.myViewModel

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.ForcastModel.Forcast
import com.example.weatherapp.Repo
import com.example.weatherapp.WeatherModel.ExampleJson2KtKotlin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MyViewModel(val repo:Repo): ViewModel() {

    private val _state=MutableLiveData<States>()
    val stateLiveData:LiveData<States>
    get() = _state
    val weather: LiveData<ExampleJson2KtKotlin>
    get() = _weather
    val forecast: LiveData<Forcast>
        get() = _forecast
    private val _weather = MutableLiveData<ExampleJson2KtKotlin>()
    private val _forecast = MutableLiveData<Forcast>()
    fun getWeather(city:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = States.Loading
                _weather.value = repo.getWeather(city)
                _state.value = States.Success
            }catch (e:Exception){
                _state.value = States.Error
            }
        }
    }
    fun getForecast(cityName:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
             _state.value = States.Loading
            _forecast.value = repo.getForecast(cityName)
            _state.value = States.Success
           }catch (e:Exception){
               _state.value = States.Error
            }
        }
    }
    fun addFavorite(name:String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addFavorite(name)
        }
    }
    fun deleteFavorite(name:String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFavorite(name)
        }
    }
    private val _allWeather = MutableLiveData<List<ExampleJson2KtKotlin>>()
    val allWeather: LiveData<List<ExampleJson2KtKotlin>>
        get() = _allWeather

    fun getFavorite(){
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = States.Loading
            _allWeather.value = repo.getFavorite()
            _state.value = States.Success
        }
    }
}
enum class States{
    Loading,
    Success,
    Error
}