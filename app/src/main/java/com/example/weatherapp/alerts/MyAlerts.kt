package com.example.weatherapp.alerts

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity

@Entity(tableName = "Alerts", primaryKeys = ["lat","lon"])
data class MyAlerts(
    var name:String,
    var id: Int=0,
    var type: Int,
    var start:Long,
    var end:Long,
    var lat:Double,
    var lon:Double,
    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(id)
        parcel.writeInt(type)
        parcel.writeLong(start)
        parcel.writeLong(end)
        parcel.writeDouble(lat)
        parcel.writeDouble(lon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyAlerts> {
        override fun createFromParcel(parcel: Parcel): MyAlerts {
            return MyAlerts(parcel)
        }

        override fun newArray(size: Int): Array<MyAlerts?> {
            return arrayOfNulls(size)
        }
    }
}
