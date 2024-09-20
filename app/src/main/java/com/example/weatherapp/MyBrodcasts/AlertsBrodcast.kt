package com.example.weatherapp.MyBrodcasts

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weatherapp.AppViews.consts
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.MyNetwork.API
import com.example.weatherapp.R
import com.example.weatherapp.Repo
import com.example.weatherapp.WeatherModel.ExampleJson2KtKotlin
import com.example.weatherapp.WeatherModel.Weather
import com.example.weatherapp.createAlarm
import com.example.weatherapp.databinding.PopupfragmentBinding
import com.example.weatherapp.from_C_to_K
import com.example.weatherapp.language
import com.example.weatherapp.settings
import com.example.weatherapp.speed
import com.example.weatherapp.units
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class AlertsBrodcast:BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?):Unit= runBlocking {

        val id = p1!!.getIntExtra("id", -1)
        val repo = Repo.getInstance(
            LocalDataSource(ForecastDataBase.getDatabase(context!!).yourDao()),
            RemoteDataSource(API)
        )
        val alert = repo.getAlert(id)
        var i = 0
        repo.getWeather(alert.city).collect {
            if (it==null){
                // no item found
                return@collect
            }
            i = it.id!!
            // set date to the start of today
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            if (calendar.timeInMillis>it.dt!!){

                // no connection since yesterday weather

                createNotificationChannel(context)
                sendNotification(context,context.getString(R.string.expire),it.name)
                return@collect
            }
            //get data with right language
            repo.getWeather(
                i,
                context.getSharedPreferences(settings, MODE_PRIVATE)
                    .getInt(language, consts.en.ordinal)
            ).collect {
                val speedUnits = context.getSharedPreferences(settings, Context.MODE_PRIVATE).getInt(speed, consts.MS.ordinal)
                val tempUnits = context.getSharedPreferences(settings, Context.MODE_PRIVATE).getInt(units, consts.C.ordinal)
                val speed = when (speedUnits) {
                    consts.MS.ordinal -> "${it.wind?.speed!!} ${context.getString(R.string.MS)}"
                    consts.MH.ordinal -> "${from_C_to_K(it.wind?.speed!!)} ${
                        context.getString(
                            R.string.MH
                        )
                    }"
                    else -> "Meter / Sec"
                }
                val temp = when (tempUnits) {
                    consts.C.ordinal -> "${it.main?.temp} ${context.getString(R.string.C)}"
                    consts.K.ordinal -> "${from_C_to_K(it.main?.temp!!)} ${
                        context.getString(
                            R.string.K
                        )
                    }"

                    consts.F.ordinal -> "${from_C_to_K(it.main?.temp!!)} ${
                        context.getString(
                            R.string.F
                        )
                    }"

                    else -> "Celsius"
                }
                when (alert.type) {
                    1 -> {
                        createNotificationChannel(context)
                        sendNotification(context, "$temp $speed", it.name)
                    }

                    2 -> {
                        if (Settings.canDrawOverlays(context)) {
                            val windowManager =
                                context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                            val params = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                // For Android 8.0 (API level 26) and above
                                WindowManager.LayoutParams(
                                    WindowManager.LayoutParams.WRAP_CONTENT,
                                    WindowManager.LayoutParams.WRAP_CONTENT,
                                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                    PixelFormat.TRANSLUCENT
                                )
                            } else {
                                // For Android versions below 8.0 (API level 26)
                                WindowManager.LayoutParams(
                                    WindowManager.LayoutParams.WRAP_CONTENT,
                                    WindowManager.LayoutParams.WRAP_CONTENT,
                                    WindowManager.LayoutParams.TYPE_PHONE,  // For versions lower than 26
                                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                    PixelFormat.TRANSLUCENT
                                )
                            }
                            params.gravity =
                                Gravity.TOP or Gravity.START  // Position the overlay at the top-left corner
                            params.x = 0
                            params.y = 0

                            val db = PopupfragmentBinding.inflate(LayoutInflater.from(context))
                            db.city.text = it.name
                            db.temp.text = temp
                            db.speed.text = speed
                            db.button.setOnClickListener {
                                windowManager.removeView(db.root)
                            }
                            windowManager.addView(db.root, params)
                        }
                    }
                }
            }
            // add day to current time  and compare if the time in next day
            val tomorrow = Calendar.getInstance()
            tomorrow.add(Calendar.DAY_OF_YEAR, 1)
            val time = tomorrow.timeInMillis-1000*60*2
            if (time<alert.end){
                createAlarm(context,time)
            }else{
                createAlarm(context,alert.end)
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
        // Build the notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
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

}