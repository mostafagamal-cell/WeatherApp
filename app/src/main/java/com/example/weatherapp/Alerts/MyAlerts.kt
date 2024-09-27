package com.example.weatherapp.Alerts

import androidx.room.Entity
import androidx.room.PrimaryKey

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