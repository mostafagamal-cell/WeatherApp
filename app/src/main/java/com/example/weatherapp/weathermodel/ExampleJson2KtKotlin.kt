package com.example.weatherapp.weathermodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "myweather",primaryKeys = ["name","language"])
data class ExampleJson2KtKotlin (
  @SerializedName("coord"      ) var coord      : Coord?             = Coord(),
  @SerializedName("weather"    ) var weather    : ArrayList<Weather> = arrayListOf(),
  @SerializedName("base"       ) var base       : String?            = null,
  @SerializedName("main"       ) var main       : Main?              = Main(),
  @SerializedName("visibility" ) var visibility : Int?               = null,
  @SerializedName("wind"       ) var wind       : Wind?              = Wind(),
  @SerializedName("clouds"     ) var clouds     : Clouds?            = Clouds(),
  @SerializedName("dt"         ) var dt         : Int?               = null,
  @SerializedName("sys"        ) var sys        : Sys?               = Sys(),
  @SerializedName("timezone"   ) var timezone   : Int?               = null,
  @SerializedName("id"         ) var id         : Int?               = null,
  var language:Int,
  @SerializedName("name"       ) var name       : String,
  @SerializedName("cod"        ) var cod        : Int?               = null,
  var isFavorite: Boolean = false,
  var lat:Double=0.0,
  var lon:Double=0.0
)