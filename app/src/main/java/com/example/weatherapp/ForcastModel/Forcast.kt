package com.example.weatherapp.WeatherModel

import com.example.weatherapp.ForcastModel.City
import com.google.gson.annotations.SerializedName
import com.example.weatherapp.ForcastModel.List

data class Forcast (

  @SerializedName("cod"     ) var cod     : String?         = null,
  @SerializedName("message" ) var message : Int?            = null,
  @SerializedName("cnt"     ) var cnt     : Int?            = null,
  @SerializedName("list"    ) var list    : ArrayList<List> = arrayListOf(),
  @SerializedName("city"    ) var city    : City?           = City()

)