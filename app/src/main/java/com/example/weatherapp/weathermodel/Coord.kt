package com.example.weatherapp.weathermodel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Coord (

  @SerializedName("lon" ) var lon : Double? = null,
  @SerializedName("lat" ) var lat : Double? = null

):Serializable,Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readValue(Double::class.java.classLoader) as? Double,
    parcel.readValue(Double::class.java.classLoader) as? Double
  ) {
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeValue(lon)
    parcel.writeValue(lat)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Coord> {
    override fun createFromParcel(parcel: Parcel): Coord {
      return Coord(parcel)
    }

    override fun newArray(size: Int): Array<Coord?> {
      return arrayOfNulls(size)
    }
  }
}