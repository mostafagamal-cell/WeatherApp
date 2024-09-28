package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.weatherapp.alerts.MyAlerts
import com.example.weatherapp.AppViews.consts
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import java.text.SimpleDateFormat

@BindingAdapter("setTemp")
fun setTemp(textView: TextView, temp: Double) {
    val context=textView.context
    when(context.getSharedPreferences(settings, Context.MODE_PRIVATE).getInt(units,consts.C.ordinal)){
        consts.C.ordinal->textView.text= "${from_C_to_K(temp)} °C"
        consts.F.ordinal->textView.text="${from_C_to_F(temp)} °F"
        consts.K.ordinal->textView.text="${temp} °K"
    }
}
@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("setImage")
fun setImage(textView: ImageView, image: String?) {
        val id=when(image){
            "01d"->R.drawable.p01d
            "01n"->R.drawable.p01n
            "02d"->R.drawable.p02d
            "02n"->R.drawable.p02n
            "03d"->R.drawable.p03d
            "03n"->R.drawable.p03n
            "04d"->R.drawable.p04d
            "04n"->R.drawable.p04n
            "09d"->R.drawable.p10d
            "09n"->R.drawable.p10n
            "10d"->R.drawable.p10d
            "10n"->R.drawable.p10n
            "11d"->R.drawable.p11d
            "11n"->R.drawable.p11n
            "13d"->R.drawable.p13d
            "13n"->R.drawable.p13n
            "50d"->R.drawable.p50d
            "50n"->R.drawable.p50n
            else->R.drawable.p01d
        }
        val context=textView.context
        Glide.with(textView.context).load(context.getDrawable(id)).into(textView)
}
@BindingAdapter("setSpeed")
fun setspeed(textView: TextView, temp: Double) {
    val context=textView.context
    when(context.getSharedPreferences(settings, Context.MODE_PRIVATE).getInt(speed,consts.MS.ordinal)){
        consts.MS.ordinal->textView.text= "$temp ${context.getString(R.string.MS)}"
        consts.MH.ordinal->textView.text= "${from_MS_to_MH(temp)} ${context.getString(R.string.MH)}"
    }
}
@BindingAdapter("setHumidity")
fun setHumidity(textView: TextView, humidity: Double) {
    textView.text= "$humidity %"
}
@BindingAdapter("setPressure")
fun setPressure(textView: TextView, pressure: Double) {
    textView.text= "$pressure hPa"
}
@BindingAdapter("setCloud")
fun setCloud(textView: TextView, cloud: Int) {
    textView.text= "$cloud %"
}
@SuppressLint("NewApi")
@BindingAdapter("setDate")
fun setDate(textView: TextView, dateInMillis: ExampleJson2KtKotlin) {
    textView.text =convertUnixToDateTime3(dateInMillis.dt!!,getTimeZoneFromOffset(dateInMillis.timezone!!))
}
@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("setTime")
fun setTime(textView: TextView, dateInMillis: ExampleJson2KtKotlin) {
    textView.text =convertUnixToDateTime2(dateInMillis.dt!!,getTimeZoneFromOffset(dateInMillis.timezone!!))
}
@BindingAdapter("setTime2")
fun setTime2(textView: TextView, dateInMillis: String) {
    textView.text =dateInMillis.split(" ").get(0)
}
@BindingAdapter("tempMax", "tempMin")

fun setTempMaxMin(textView: TextView, tempmax: Double ,tempmin: Double) {
    val context=textView.context
    when(context.getSharedPreferences(settings, Context.MODE_PRIVATE).getInt(units,consts.C.ordinal)){
        consts.C.ordinal->textView.text= "${from_C_to_K(tempmax)} / ${from_C_to_K(tempmin)} °C"
        consts.F.ordinal->textView.text="${from_C_to_F(tempmax)}  / ${from_C_to_F(tempmin)} °F"
        consts.K.ordinal->textView.text="${tempmax} / ${tempmin} °K"
    }
}
@BindingAdapter("setAlert")
fun setAlert(textView: TextView, alert: MyAlerts) {
    val date=SimpleDateFormat("yyyy-MM-dd").format(alert.start)
    val date2=SimpleDateFormat("yyyy-MM-dd").format(alert.end)
    textView.text="${alert.type} ${alert.name} ${date} --> ${date2}"
}
