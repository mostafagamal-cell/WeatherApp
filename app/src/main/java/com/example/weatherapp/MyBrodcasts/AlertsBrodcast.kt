package com.example.weatherapp.MyBrodcasts

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weatherapp.AppViews.SettingsFragment
import com.example.weatherapp.AppViews.SettingsFragment.Companion.setLocale
import com.example.weatherapp.AppViews.consts
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.MyNetwork.API
import com.example.weatherapp.R
import com.example.weatherapp.Repo
import com.example.weatherapp.alerts.MyAlerts
import com.example.weatherapp.createAlarm
import com.example.weatherapp.databinding.PopupfragmentBinding
import com.example.weatherapp.from_C_to_F
import com.example.weatherapp.from_C_to_K
import com.example.weatherapp.from_MS_to_MH
import com.example.weatherapp.language
import com.example.weatherapp.notification
import com.example.weatherapp.settings
import com.example.weatherapp.speed
import com.example.weatherapp.units
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import java.util.Locale

class AlertsBrodcast:BroadcastReceiver() {
    var job: Job?=null
    var job2: Job?=null
    override fun onReceive(context1: Context?, p1: Intent?):Unit= runBlocking {
        context1!!
        val id1 = p1!!.getStringExtra("id")
        val gson = com.google.gson.Gson()
        val id = gson.fromJson(id1, MyAlerts::class.java)
        val repo = Repo.getInstance(
            LocalDataSource(ForecastDataBase.getDatabase(context1).yourDao()),
            RemoteDataSource(API)
        )
        var context:Context?=context1
        if(context1.getSharedPreferences(settings, MODE_PRIVATE)
            .getInt(language, consts.en.ordinal)==consts.ar.ordinal){
            SettingsFragment.setLocale( "ar",context1.applicationContext)
            setLocale1(context1,"ar")
            Log.i("the arabic new context",context!!.getString(R.string.alarms))

        }
        if(context1.getSharedPreferences(settings, MODE_PRIVATE)
                .getInt(language, consts.en.ordinal)==consts.en.ordinal){
            SettingsFragment.setLocale( "en",context1.applicationContext)
            setLocale1(context1,"en")
            Log.i("the english new context",context!!.getString(R.string.alarms))

        }

        context!!

        val alert=  repo.getAlert(id.id).firstOrNull()
                   if (alert != null) {
                       if (context.getSharedPreferences(settings, Context.MODE_PRIVATE)
                               .getInt(notification, consts.enable.ordinal) == consts.enable.ordinal
                       ) {
                           job = launch {
                               repo.getWeather(
                                   alert.lat,
                                   alert.lon,
                                   context.getSharedPreferences(settings, MODE_PRIVATE)
                                       .getInt(language, consts.en.ordinal)
                               ).collect {
                                   if (it == null) {
                                       job?.cancel()
                                       return@collect
                                   }
                                   // set date to the start of today
                                   val calendar = Calendar.getInstance().apply {
                                       set(Calendar.HOUR_OF_DAY, 0)
                                       set(Calendar.MINUTE, 0)
                                       set(Calendar.SECOND, 0)
                                       set(Calendar.MILLISECOND, 0)
                                   }
                                   if (calendar.timeInMillis > it.dt!! * 1000) {
                                       // no connection since yesterday weather
                                       createNotificationChannel(context)
                                       sendNotification(
                                           context,
                                           context.getString(R.string.expire),
                                           it.name
                                       )
                                       job?.cancel()
                                       return@collect
                                   }
                                   repo.getWeather(
                                       alert.lat, alert.lon,
                                       context.getSharedPreferences(settings, MODE_PRIVATE)
                                           .getInt(language, consts.en.ordinal)
                                   ).collect {
                                       val speedUnits =
                                           context.getSharedPreferences(
                                               settings,
                                               Context.MODE_PRIVATE
                                           )
                                               .getInt(speed, consts.MS.ordinal)
                                       val tempUnits =
                                           context.getSharedPreferences(
                                               settings,
                                               Context.MODE_PRIVATE
                                           )
                                               .getInt(units, consts.C.ordinal)
                                       val speed = when (speedUnits) {
                                           consts.MS.ordinal -> "${it?.wind?.speed!!} ${
                                                   context.getString(
                                                       R.string.MS
                                                   )
                                           }"

                                           consts.MH.ordinal -> "${from_MS_to_MH(it?.wind?.speed!!)} ${
                                                      context.getString(
                                                       R.string.MH
                                                   )
                                            
                                           }"

                                           else -> "Meter / Sec"
                                       }
                                       val temp = when (tempUnits) {
                                           consts.C.ordinal -> "${from_C_to_K(it?.main?.temp!!)} ${
                                                   context.getString(
                                                   R.string.C
                                               )
                                           }"

                                           consts.K.ordinal -> "${it?.main?.temp!!} ${
                                                      context.getString(
                                                   R.string.K
                                               )
                                           }"

                                           consts.F.ordinal -> "${from_C_to_F(it?.main?.temp!!)} ${
                                             
                                                   context.getString(
                                                       R.string.F
                                                   )
                                                   }"

                                           else -> "${context.getString(R.string.C)}"
                                       }
                                       when (alert.type) {
                                           1 -> {
                                               Log.i(
                                                   "dddddddddddddddddddddddd",
                                                   "${temp} ${speed} ${it?.name}"
                                               )
                                               createNotificationChannel(context)
                                               sendNotification(context, "$temp $speed", it?.name?:"")
                                               job?.cancel()
                                           }
                                           2 -> {
                                               if (Settings.canDrawOverlays(context)) {
                                                   // Prepare data for the overlay
                                                   val overlayIntent =
                                                       Intent(
                                                           context,
                                                           OverlayServices::class.java
                                                       ).apply {
                                                           putExtra("city", it?.name)
                                                           putExtra("temp", temp)
                                                           putExtra("speed", speed)
                                                       }
                                                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                       context.startForegroundService(overlayIntent)
                                                   } else {
                                                       context.startService(overlayIntent)
                                                   }
                                               }
                                               job?.cancel()
                                           }

                                       }
                                   }
                               }

                           }
                       }
                       val tomorrow = Calendar.getInstance()
                       tomorrow.add(Calendar.DAY_OF_YEAR, 1)
                       val time = tomorrow.timeInMillis
                       Log.i("created alarm manager", "Success  ${time}  ${alert}")
                       if (time < alert.end) {
                           Log.i("aaaaaaaaaaaaaaaaa", "vvvvvvvvvvvvvvvvv")
                           createAlarm(context, alert)
                       } else if (alert.end > Calendar.getInstance().timeInMillis) {
                           Log.i("aaaaaaaaaaaaaaaaa", "dddddddddddddddd")
                           createAlarm(context, alert, true)
                       } else {
                           Log.i("aaaaaaaaaaaaaaaaa", "Zzzzzzzzzzzzzzzzzzzzz")
                           repo.deleteAlert(alert)
                       }

                   }

    }

      val CHANNEL_ID = "channel_id_example_01"
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = CHANNEL_ID
            val channelName = context.getString(R.string.alarms)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = context.getString(R.string.alarms)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    fun sendNotification(context: Context,message:String,title:String) {
        val notificationId = 1
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.p02d)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        notificationManager.notify(notificationId, builder.build())
    }

    private fun setLocale1(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}