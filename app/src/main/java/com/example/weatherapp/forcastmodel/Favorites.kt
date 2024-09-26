package com.example.weatherapp.forcastmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity

@Entity(tableName = "favorites",primaryKeys = ["lat","lon"])
data class Favorites(
    val name:String,
    val lat:Double,
    val lon:Double,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(lat)
        parcel.writeDouble(lon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Favorites> {
        override fun createFromParcel(parcel: Parcel): Favorites {
            return Favorites(parcel)
        }

        override fun newArray(size: Int): Array<Favorites?> {
            return arrayOfNulls(size)
        }
    }
}