package com.example.weatherapp.weathermodel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Wind (

  @SerializedName("speed" ) var speed : Double? = null,
  @SerializedName("deg"   ) var deg   : Int?    = null

):Serializable,Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readValue(Double::class.java.classLoader) as? Double,
    parcel.readValue(Int::class.java.classLoader) as? Int
  ) {
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeValue(speed)
    parcel.writeValue(deg)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Wind> {
    override fun createFromParcel(parcel: Parcel): Wind {
      return Wind(parcel)
    }

    override fun newArray(size: Int): Array<Wind?> {
      return arrayOfNulls(size)
    }
  }
}