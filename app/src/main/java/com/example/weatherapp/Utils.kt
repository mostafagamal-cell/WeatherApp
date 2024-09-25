package com.example.weatherapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weatherapp.AppViews.consts
import com.example.weatherapp.MyBrodcasts.AlertsBrodcast
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import kotlin.math.round

enum class States {
    NONE,
    Loading,
    Success,
    Error
}
fun from_C_to_K(double: Double)= (double-273.15).round(2)
fun from_C_to_F(double: Double)=((double - 273.15) * 9/5 + 32) .round(2)
fun from_MS_to_MH(double: Double)=(double*2.23694).round(2)
const val pickec="pickec"
const val cities="cities"
const val current="current"
const val forecast="forecast"
const val weather="weather"
const val mode="mode"
const val map="map"
const val units="units"
const val speed="speed"
const val notification="notification"
const val language="language"
const val settings="settings"
const val nextid="nextid"
const val lat="latitude"
const val longite="longitude"
fun createAlarm(context: Context, startDate:Long){
    var id= context.getSharedPreferences(nextid,Context.MODE_PRIVATE).getInt("id",0)
    val alarm=context.getSystemService(android.app.AlarmManager::class.java) as AlarmManager
    val intent= Intent(context,AlertsBrodcast::class.java)
    intent.putExtra("id",id)
    val pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    id++
    if (id>=Int.MAX_VALUE){
        id=0
    }
    id++
    context.getSharedPreferences(nextid,Context.MODE_PRIVATE).edit().putInt("id",id+1).apply()
    alarm.set(AlarmManager.RTC_WAKEUP, startDate, pendingIntent)
}
fun Double.round(decimals: Int): Double {
    var multiplier = 10.0.pow(decimals)
    return kotlin.math.round(this * multiplier) / multiplier
}

fun getDayNameFromTimestamp(timestamp: Long,lang:Int): String {
    val date = Date(timestamp)
    var format:SimpleDateFormat?=null
    if (lang== consts.ar.ordinal)
        format =SimpleDateFormat("EEEE dd-MM-yyyy", Locale("ar"))
    if (lang== consts.en.ordinal)
        format = SimpleDateFormat("EEEE dd-MM-yyyy", Locale("en"))
    return format!!.format(date)
}
fun getDayHourFromTimestamp(timestamp: String,lang:Int):String {
    Log.i("sssssssssiizzzz","Success  ${timestamp} ${lang==consts.ar.ordinal} ")
    val x= when (lang) {
        consts.ar.ordinal -> {
            when (timestamp) {
                "00:00:00" -> "12 ص"
                "03:00:00" -> "3 ص"
                "06:00:00" -> "6 ص"
                "09:00:00" -> "9 ص"
                "12:00:00" -> "12 م"
                "15:00:00" -> "3 م"
                "18:00:00" -> "6 م"
                "21:00:00" -> "9 م"
                else -> ""
            }
        }

        consts.en.ordinal -> {
            when (timestamp) {
                "00:00:00" -> "12 AM"
                "03:00:00" -> "3 AM"
                "06:00:00" -> "6 AM"
                "09:00:00" -> "9 AM"
                "12:00:00" -> "12 PM"
                "15:00:00" -> "3 PM"
                "18:00:00" -> "6 PM"
                "21:00:00" -> "9 PM"
                else -> ""
            }
        }

        else -> {
            ""
        }

    }
    return x
}

@RequiresApi(Build.VERSION_CODES.O)
fun getTimeZoneFromOffset(offsetInSeconds: Int): String {
    // Convert the offset from seconds to hours and minutes
    val hours = TimeUnit.SECONDS.toHours(offsetInSeconds.toLong()).toInt()
    val minutes = TimeUnit.SECONDS.toMinutes(offsetInSeconds.toLong()) % 60

    // Create a ZoneOffset from the hours and minutes
    val zoneOffset = ZoneOffset.ofHoursMinutes(hours, minutes.toInt())

    // Return the offset in a readable string format (e.g., +02:00)
    return zoneOffset.toString()
}
fun getTimeZoneFromOffset2(offsetInSeconds: Int): String {
    // Convert the offset from seconds to hours and minutes
    val hours = offsetInSeconds / 3600
    val minutes = (offsetInSeconds % 3600) / 60

    // Create a time zone string like "+02:00"
    return String.format("GMT%+02d:%02d", hours, minutes)
}
@RequiresApi(Build.VERSION_CODES.O)
fun convertUnixToDateTime2(timestamp: Long, timeZone: String): String {
    // Convert the Unix timestamp to an Instant
    val instant = Instant.ofEpochSecond(timestamp)

    // Convert the instant to a ZonedDateTime with the desired time zone
    val zonedDateTime = instant.atZone(ZoneId.of(timeZone))

    // Format the ZonedDateTime to a readable string
    val formatter = DateTimeFormatter.ofPattern("h:mm a")
    return zonedDateTime.format(formatter)
}
@RequiresApi(Build.VERSION_CODES.O)
fun convertUnixToDateTime3(timestamp: Long, timeZone: String): String {
    // Convert the Unix timestamp to an Instant
    val instant = Instant.ofEpochSecond(timestamp)

    // Convert the instant to a ZonedDateTime with the desired time zone
    val zonedDateTime = instant.atZone(ZoneId.of(timeZone))

    // Format the ZonedDateTime to a readable string
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return zonedDateTime.format(formatter)
}


