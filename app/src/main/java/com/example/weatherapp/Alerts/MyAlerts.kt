package com.example.weatherapp.Alerts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Alerts")
data class MyAlerts(
    @PrimaryKey
    var id: Int=0,
    var type: Int,
    var start:Long,
    var end:Long,
    var lat:Double,
    var lon:Double,
    )