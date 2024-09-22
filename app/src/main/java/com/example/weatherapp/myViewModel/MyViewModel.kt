package com.example.weatherapp.myViewModel

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModelFac(val localDataSource: LocalDataSource, val remoteDataSource: RemoteDataSource) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyViewModel(Repo.getInstance(localDataSource,remoteDataSource)) as T
    }
}
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

    private val _allWeather = MutableLiveData<List<ExampleJson2KtKotlin>>()
    val allWeather: LiveData<List<ExampleJson2KtKotlin>>
        get() = _allWeather
    fun getWeather(city:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = States.Loading
//                repo.getWeather(city,consts.ar.ordinal).collect {
//                    _weather.value = it
//                }
                _state.value = States.Success
            }catch (e:Exception){
                _state.value = States.Error
            }
        }
    }
    fun getForecast(lat:Double,lon:Double,lang:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
             _state.value = States.Loading
           repo.getForecast(lat,lon,lang).collect {
                 _forecast.value = it
               _state.value = States.Success
             }

           }catch (e:Exception){
               _state.value = States.Error
            }
        }
    }
    fun addFavorite(name:String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addFavorite(name)
            getFavorites()
        }
    }
    fun deleteFavorite(name:String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFavorite(name)
            getFavorites()
        }
    }
    fun getFavorites(){
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = States.Loading
            _allWeather.value = repo.getFavorite()
            _state.value = States.Success
        }
    }
    fun getMyWeather(){
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = States.Loading
            _allWeather.value = repo.getAllWeather()
            _state.value = States.Success
        }
    }
}