package com.example.weatherapp.ForcastModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.ForcastModel.City
import com.google.gson.annotations.SerializedName
import com.example.weatherapp.ForcastModel.List
@Entity(tableName = "forecast")
data class Forcast (
  var lang:String,
  @SerializedName("cod"     ) val cod     : String,
  @SerializedName("message" ) val message : Int,
  @SerializedName("cnt"     ) val cnt     : Int,
  @SerializedName("list"    ) val list    : ArrayList<List>,
  @SerializedName("city"    ) val city    : City,
  @PrimaryKey
  var cityName: String = city.name,
  var lat:Double=city.coord?.lat!!,
  var lon:Double=city.coord?.lon!!
)