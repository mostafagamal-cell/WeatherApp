package com.example.weatherapp.MyBrodcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class AlertsBrodcast:BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            val dataBase = ForecastDataBase.getDatabase(p0!!)
            val localDataSource = LocalDataSource(dataBase.yourDao())
            val id=p1!!.getIntExtra("id",0)
            val alert=localDataSource.getAlert(id)
        }
    }
}