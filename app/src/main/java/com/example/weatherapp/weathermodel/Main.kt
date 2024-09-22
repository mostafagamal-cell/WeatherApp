package com.example.weatherapp.weathermodel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Main (
  @SerializedName("temp"       ) var temp      : Double? = null,
  @SerializedName("feels_like" ) var feelsLike : Double? = null,
  @SerializedName("temp_min"   ) var tempMin   : Double? = null,
  @SerializedName("temp_max"   ) var tempMax   : Double? = null,
  @SerializedName("pressure"   ) var pressure  : Int?    = null,
  @SerializedName("humidity"   ) var humidity  : Int?    = null,
  @SerializedName("sea_level"  ) var seaLevel  : Int?    = null,
  @SerializedName("grnd_level" ) var grndLevel : Int?    = null

):Serializable,Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readValue(Double::class.java.classLoader) as? Double,
    parcel.readValue(Double::class.java.classLoader) as? Double,
    parcel.readValue(Double::class.java.classLoader) as? Double,
    parcel.readValue(Double::class.java.classLoader) as? Double,
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readValue(Int::class.java.classLoader) as? Int
  ) {
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeValue(temp)
    parcel.writeValue(feelsLike)
    parcel.writeValue(tempMin)
    parcel.writeValue(tempMax)
    parcel.writeValue(pressure)
    parcel.writeValue(humidity)
    parcel.writeValue(seaLevel)
    parcel.writeValue(grndLevel)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Main> {
    override fun createFromParcel(parcel: Parcel): Main {
      return Main(parcel)
    }

    override fun newArray(size: Int): Array<Main?> {
      return arrayOfNulls(size)
    }
  }
}