package com.example.weatherapp

import android.os.Build
import androidx.annotation.RequiresApi
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