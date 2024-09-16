package com.example.weatherapp.WeatherModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable


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
  @SerializedName("name"       ) var name       : String?            = null,
  @SerializedName("cod"        ) var cod        : Int?               = null
):Serializable,Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readParcelable(Coord::class.java.classLoader),
    parcel.createTypedArrayList(Weather.CREATOR) as ArrayList<Weather>,
    parcel.readString(),
    parcel.readParcelable(Main::class.java.classLoader),
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readParcelable(Wind::class.java.classLoader),
    parcel.readParcelable(Clouds::class.java.classLoader),
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readParcelable(Sys::class.java.classLoader),
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readString(),
    parcel.readValue(Int::class.java.classLoader) as? Int
  ) {
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeParcelable(coord, flags)
    parcel.writeString(base)
    parcel.writeParcelable(main, flags)
    parcel.writeValue(visibility)
    parcel.writeParcelable(wind, flags)
    parcel.writeParcelable(clouds, flags)
    parcel.writeValue(dt)
    parcel.writeParcelable(sys, flags)
    parcel.writeValue(timezone)
    parcel.writeValue(id)
    parcel.writeString(name)
    parcel.writeValue(cod)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<ExampleJson2KtKotlin> {
    override fun createFromParcel(parcel: Parcel): ExampleJson2KtKotlin {
      return ExampleJson2KtKotlin(parcel)
    }

    override fun newArray(size: Int): Array<ExampleJson2KtKotlin?> {
      return arrayOfNulls(size)
    }
  }
}