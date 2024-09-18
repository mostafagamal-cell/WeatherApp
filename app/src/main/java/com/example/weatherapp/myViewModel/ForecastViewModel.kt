package com.example.weatherapp.myViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.ForcastModel.Forcast
import com.example.weatherapp.Repo
import com.example.weatherapp.States
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForecastViewModelFac(val localDataSource: LocalDataSource, val remoteDataSource: RemoteDataSource) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyViewModel(Repo(localDataSource,remoteDataSource)) as T
    }
}
class ForecastViewModel(val repo: Repo):ViewModel() {
    private val _state= MutableLiveData<States>()
    private val _forecast= MutableLiveData<Forcast>()
    val forecast:MutableLiveData<Forcast>
        get()=_forecast
    val state: LiveData<States>get() = _state
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
}