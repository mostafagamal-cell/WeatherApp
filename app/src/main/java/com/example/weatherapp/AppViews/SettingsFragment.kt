package com.example.weatherapp.AppViews

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.util.Util
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSettingsBinding

import com.example.weatherapp.*

 enum class consts{
    ar,
    en,
    C,
    K,
    F,
    MS,
    MH,
    enable,
    disable,
    GPS,
    Map
}

class SettingsFragment : Fragment() {
   lateinit var db:FragmentSettingsBinding
    val req=100
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db= FragmentSettingsBinding.inflate(layoutInflater)
        return db.root
    }



    private fun setup() {
        val pref = requireActivity().getSharedPreferences(settings, Context.MODE_PRIVATE)
        val mymode = pref.getInt(mode, consts.Map.ordinal)
        val myunite = pref.getInt(units, consts.C.ordinal)
        val mylanguage = pref.getInt(language, consts.en.ordinal)
        val myspeed=pref.getInt(speed,consts.MS.ordinal)
        val mynotification = pref.getInt(notification, consts.enable.ordinal)
        if (myspeed==consts.MS.ordinal){
            db.MS.isChecked=true
        }else{
            db.MH.isChecked=true
        }
        if (mynotification == consts.enable.ordinal) {
            db.enablenf.isChecked = true
        } else {
            db.disablenf.isChecked = true
        }
        if (mymode == consts.GPS.ordinal &&checkpermessions()) {
                db.GPSrdb.isChecked = true
        } else {
            db.Map.isChecked = true
        }
        if (mylanguage == consts.en.ordinal) {
            db.Englishrdb.isChecked = true
        } else {
            db.arabicrdb.isChecked = true
        }
        if (myunite == consts.C.ordinal) {
            db.Crdb.isChecked = true
        } else if (myunite == consts.K.ordinal) {
            db.Krdb.isChecked = true
        } else {
            db.Frdb.isChecked = true
        }
        db.SpeenUnits.setOnCheckedChangeListener { e, c ->
            if (db.MS.isChecked) {
                pref.edit().putInt(speed, consts.MS.ordinal).apply()
            }
            if (db.MH.isChecked) {
                pref.edit().putInt(speed, consts.MH.ordinal).apply()
            }
        }
        db.notification.setOnCheckedChangeListener { e, c ->
            if (db.enablenf.isChecked) {
                pref.edit().putInt(notification, consts.enable.ordinal).apply()
            }
            if (db.disablenf.isChecked) {
                pref.edit().putInt(notification, consts.disable.ordinal).apply()
            }
        }
        db.languge.setOnCheckedChangeListener { _, _ ->
            val currentLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context?.getSystemService(LocaleManager::class.java)?.applicationLocales?.toLanguageTags()
            } else {
                AppCompatDelegate.getApplicationLocales().toLanguageTags()
            }

            if (db.Englishrdb.isChecked && currentLocale != "en") {
                pref.edit().putInt(language, consts.en.ordinal).apply()
                setLocale("en",requireContext())
            }

            if (db.arabicrdb.isChecked && currentLocale != "ar") {
                pref.edit().putInt(language, consts.ar.ordinal).apply()
               setLocale("ar",requireContext())
            }
            recreate(requireActivity())
        }


        db.enablenf.setOnCheckedChangeListener { e, c ->
            if (db.enablenf.isChecked) {
                pref.edit().putInt(notification, consts.enable.ordinal).apply()
            }
            if (db.disablenf.isChecked) {
                pref.edit().putInt(notification, consts.disable.ordinal).apply()
            }
        }

        db.Temp.setOnCheckedChangeListener { e, c ->
            if (db.Crdb.isChecked) {
                pref.edit().putInt(units, consts.C.ordinal).apply()
            }
            if (db.Krdb.isChecked) {
                pref.edit().putInt(units, consts.K.ordinal).apply()
            }
            if(db.Frdb.isChecked) {
                pref.edit().putInt(units, consts.F.ordinal).apply()
            }
        }

        db.mode.setOnCheckedChangeListener{ e,c->
            if (db.GPSrdb.isChecked){
                if (!checkpermessions()) {
                    startGPS()
                    return@setOnCheckedChangeListener
                }
                pref.edit().putInt(mode,consts.GPS.ordinal).apply()
            }
            if (db.Map.isChecked){
                pref.edit().putInt(mode,consts.Map.ordinal).apply()
            }
        }

    }
    override fun onResume() {
        super.onResume()
        setup()
    }

    val per=arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ,android.Manifest.permission.ACCESS_COARSE_LOCATION
    ,android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
    ,android.Manifest.permission.SET_ALARM
    ,android.Manifest.permission.SCHEDULE_EXACT_ALARM
    ,android.Manifest.permission.INTERNET
    ,android.Manifest.permission.POST_NOTIFICATIONS
    )
   fun requestPermessions(){
       requestPermissions(per,req)
   }
   fun startGPS(){
       AlertDialog.Builder(this.requireContext())
           .setTitle(this.getString(R.string.request_GPS_permissions))
           .setMessage(this.getString(R.string.please_Give_GPS_permissions))
           .setPositiveButton(this.getString(R.string.ok)) { _, _ ->
               requestPermessions()
           }
           .setNegativeButton(this.getString(R.string.cancel)) { e, c ->
               db.Map.isChecked=true
               e.dismiss()
           }.create().show()
   }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val pref = requireActivity().getSharedPreferences(settings, Context.MODE_PRIVATE)
        if (requestCode==req){
            val t=PackageManager.PERMISSION_GRANTED
            if (grantResults[0]==t){
                db.GPSrdb.isChecked=true
                pref.edit().putInt(mode,consts.GPS.ordinal).apply()
            }else{
                db.Map.isChecked=true
                pref.edit().putInt(mode,consts.Map.ordinal).apply()
                val Snak=AlertDialog.Builder(this.requireContext())
                    .setMessage(this.getString(R.string.faild_GPS_permissions))
                    .setPositiveButton(R.string.ok){e,c->
                        e.dismiss()
                    }.create().show()
            }
        }
    }
     fun checkpermessions():Boolean{
        val b=this.requireContext().checkSelfPermission(per[0]) == PackageManager.PERMISSION_GRANTED||this.requireContext().checkSelfPermission(per[1]) == PackageManager.PERMISSION_GRANTED
        return b
    }
    companion object {
        fun setLocale(languageTag: String,context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.getSystemService(LocaleManager::class.java)?.applicationLocales =
                    LocaleList.forLanguageTags(languageTag)
            } else {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTag))
            }
        }

        fun restartApp(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
            (context as Activity).finish()
        }
    }
}