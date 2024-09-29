package com.example.weatherapp.MyBrodcasts

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.example.weatherapp.AppViews.consts
import com.example.weatherapp.R
import com.example.weatherapp.databinding.PopupfragmentBinding
import com.example.weatherapp.language
import com.example.weatherapp.settings
class OverlayServices : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private var overlayView: View? = null // Variable to hold the reference to the current overlay
    private lateinit var windowManager: WindowManager // WindowManager variable

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("OverlayService", "onStartCommand: ")

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val params = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }

        // Center the overlay in the middle of the screen
        params.gravity = Gravity.CENTER

        // Remove the previous overlay if it exists
        if (overlayView != null) {
            windowManager.removeView(overlayView) // Remove the previous overlay
            overlayView = null // Clear the reference
        }

        // Inflate the layout
        val inflater = LayoutInflater.from(this)
        val binding = PopupfragmentBinding.inflate(inflater)

        // Set background to gray
        binding.root.setBackgroundColor(getColor(R.color.gray))

        // Set text from intent extras
        binding.city.text = intent?.getStringExtra("city")
        binding.temp.text = intent?.getStringExtra("temp")
        binding.speed.text = intent?.getStringExtra("speed")

        // Button to close the overlay and stop sound
        binding.button.setOnClickListener {
            windowManager.removeView(binding.root)
            mediaPlayer.stop()
            mediaPlayer.release()
            overlayView = null // Clear the reference to the overlay
            stopSelf()
        }

        // Play sound when the overlay appears
        mediaPlayer = MediaPlayer.create(this, R.raw.soubd) // Replace with your sound file
        mediaPlayer.start()

        createNotificationChannel(this)

        // Create and display notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Weather Alert")
            .setContentText("Displaying weather overlay")
            .setSmallIcon(R.drawable.p03d)
            .build()

        // Add the new view to the window and store the reference
        windowManager.addView(binding.root, params)
        overlayView = binding.root // Save reference to the overlay view

        startForeground(1, notification)

        return START_NOT_STICKY
    }

    private val CHANNEL_ID = "channel_id_example_01"

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

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        if (overlayView != null) {
            windowManager.removeView(overlayView) // Remove the view if it exists
            overlayView = null
        }
    }
}
