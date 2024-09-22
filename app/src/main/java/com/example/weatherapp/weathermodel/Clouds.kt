package com.example.weatherapp.weathermodel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Clouds (
  @SerializedName("all" ) var all : Int? = null
): Serializable, Parcelable {
  constructor(parcel: Parcel) : this(parcel.readValue(Int::class.java.classLoader) as? Int) {
  }

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(p0: Parcel, p1: Int) {
    all?.let {
     p0.writeInt(it)
   }
  }

  companion object CREATOR : Parcelable.Creator<Clouds> {
    override fun createFromParcel(parcel: Parcel): Clouds {
      return Clouds(parcel)
    }

    override fun newArray(size: Int): Array<Clouds?> {
      return arrayOfNulls(size)
    }
  }
}