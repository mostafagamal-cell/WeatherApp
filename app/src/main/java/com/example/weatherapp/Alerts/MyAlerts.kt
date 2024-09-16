package com.example.weatherapp.Alerts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Alerts")
data class MyAlerts(
    @PrimaryKey(autoGenerate = true)
    var id: Int=0,
    var type: Int,
    var start:String,
    var end:String,
    var city:String,
    )