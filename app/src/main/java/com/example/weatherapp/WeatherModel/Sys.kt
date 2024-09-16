package com.example.weatherapp.WeatherModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Sys (

  @SerializedName("type"    ) var type    : Int?    = null,
  @SerializedName("id"      ) var id      : Int?    = null,
  @SerializedName("country" ) var country : String? = null,
  @SerializedName("sunrise" ) var sunrise : Int?    = null,
  @SerializedName("sunset"  ) var sunset  : Int?    = null

):Serializable,Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readString(),
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readValue(Int::class.java.classLoader) as? Int
  ) {
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeValue(type)
    parcel.writeValue(id)
    parcel.writeString(country)
    parcel.writeValue(sunrise)
    parcel.writeValue(sunset)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Sys> {
    override fun createFromParcel(parcel: Parcel): Sys {
      return Sys(parcel)
    }

    override fun newArray(size: Int): Array<Sys?> {
      return arrayOfNulls(size)
    }
  }
}