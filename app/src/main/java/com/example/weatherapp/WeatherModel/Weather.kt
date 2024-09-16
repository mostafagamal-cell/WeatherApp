package com.example.weatherapp.WeatherModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Weather (

  @SerializedName("id"          ) var id          : Int?    = null,
  @SerializedName("main"        ) var main        : String? = null,
  @SerializedName("description" ) var description : String? = null,
  @SerializedName("icon"        ) var icon        : String? = null

):Serializable,Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readString(),
    parcel.readString(),
    parcel.readString()
  ) {
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeValue(id)
    parcel.writeString(main)
    parcel.writeString(description)
    parcel.writeString(icon)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Weather> {
    override fun createFromParcel(parcel: Parcel): Weather {
      return Weather(parcel)
    }

    override fun newArray(size: Int): Array<Weather?> {
      return arrayOfNulls(size)
    }
  }
}