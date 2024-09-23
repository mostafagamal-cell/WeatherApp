package com.example.weatherapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherapp.MyBrodcasts.AlertsBrodcast
import org.json.JSONObject
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Calendar

enum class States {
    NONE,
    Loading,
    Success,
    Error
}
fun from_C_to_K(double: Double)=double-273.15
fun from_C_to_F(double: Double)=double-459.67
fun from_MS_to_MH(double: Double)=double*2.23694
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
    //not to over flow

    if (id>=Int.MAX_VALUE){
        id=0
    }
    id++
    context.getSharedPreferences(nextid,Context.MODE_PRIVATE).edit().putInt("id",id+1).apply()
    alarm.set(AlarmManager.RTC_WAKEUP, startDate, pendingIntent)
}

