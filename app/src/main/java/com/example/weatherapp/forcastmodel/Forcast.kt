package com.example.weatherapp.forcastmodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.language
import com.google.gson.annotations.SerializedName

@Entity(tableName = "forecast",primaryKeys = ["cityName","lang"])


data class Forcast (
  var lang:Int,
  @SerializedName("cod"     ) val cod     : String,
  @SerializedName("message" ) val message : Int,
  @SerializedName("cnt"     ) val cnt     : Int,
  @SerializedName("list"    ) var list    : ArrayList<List>,
  @SerializedName("city"    ) val city    : City,
  var cityName: String = city.name,
  var lat:Double=city.coord?.lat!!,
  var lon:Double=city.coord?.lon!!
)