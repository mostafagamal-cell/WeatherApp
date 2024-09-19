package com.example.weatherapp.WeatherModel

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.intellij.lang.annotations.Language
import java.io.Serializable

@Entity(tableName = "myweather")
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
  @PrimaryKey
  @SerializedName("name"       ) var name       : String,
  @SerializedName("cod"        ) var cod        : Int?               = null,
  var isFavorite: Boolean = false
)