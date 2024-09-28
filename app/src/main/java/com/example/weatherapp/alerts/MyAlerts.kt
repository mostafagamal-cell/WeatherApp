package com.example.weatherapp.alerts

import androidx.room.Entity

@Entity(tableName = "Alerts", primaryKeys = ["lat","lon"])
data class MyAlerts(
    var name:String,
    var id: Int=0,
    var type: Int,
    var start:Long,
    var end:Long,
    var lat:Double,
    var lon:Double,
    )