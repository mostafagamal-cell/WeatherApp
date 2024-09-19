package com.example.weatherapp.AppViews

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSettingsBinding

import com.example.weatherapp.*


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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    private fun setup() {
        val pref = requireActivity().getSharedPreferences(settings, Context.MODE_PRIVATE)
        val mode = pref.getInt(mode, 1)
        val unite = pref.getInt(units, 1)
        val mylanguage = pref.getInt(language, 1)
        val myspeed=pref.getInt(speed,1)
        val mynotification = pref.getInt(notification, 1)
        if (myspeed==1){
            db.MS.isChecked=true
        }else{
            db.MH.isChecked=true
        }
        if (mynotification == 1) {
            db.enablenf.isChecked = true
        } else {
            db.disablenf.isChecked = true
        }
        if (mode == 1) {
            if (checkpermessions()){
                db.GPSrdb.isChecked = true
            }
        } else {
            db.Map.isChecked = true
        }
        if (mylanguage == 1) {
            db.Englishrdb.isChecked = true
        } else {
            db.arabicrdb.isChecked = true
        }
        if (unite == 1) {
            db.Crdb.isChecked = true
        } else if (unite == 2) {
            db.Krdb.isChecked = true
        } else {
            db.Frdb.isChecked = true
        }
        db.SpeenUnits.setOnCheckedChangeListener { e, c ->

            if (db.MS.isChecked) {
                pref.edit().putInt(speed, 1).apply()
            }
            if (db.MH.isChecked) {
                pref.edit().putInt(speed, 2).apply()
            }
        }
        db.notification.setOnCheckedChangeListener { e, c ->
            if (db.enablenf.isChecked) {
                pref.edit().putInt(notification, 1).apply()
            }
            if (db.disablenf.isChecked) {
                pref.edit().putInt(notification, 2).apply()
            }
        }
        db.languge.setOnCheckedChangeListener { e, c ->
            if (db.Englishrdb.isChecked) {
                pref.edit().putInt(language, 1).apply()
            }
            if (db.arabicrdb.isChecked) {
                pref.edit().putInt(language, 2).apply()
            }
        }

        db.enablenf.setOnCheckedChangeListener { e, c ->
            if (db.enablenf.isChecked) {
                pref.edit().putInt(notification, 1).apply()
            }
            if (db.disablenf.isChecked) {
                pref.edit().putInt(notification, 2).apply()
            }
        }

        db.Temp.setOnCheckedChangeListener { e, c ->
            if (db.Crdb.isChecked) {
                pref.edit().putInt(units, 1).apply()
            }
            if (db.Krdb.isChecked) {
                pref.edit().putInt(units, 2).apply()
            }
            if(db.Frdb.isChecked) {
                pref.edit().putInt(units, 3).apply()
            }
        }

        db.mode.setOnCheckedChangeListener{ e,c->
            if (db.GPSrdb.isChecked){
                pref.edit().putInt("mode",1).apply()
                if (!checkpermessions()){
                    startGPS()
                }else{
                    Toast.makeText(this.requireContext(),"ok",Toast.LENGTH_SHORT).show()
                }
            }
            if (db.Map.isChecked){
                pref.edit().putInt("mode",2).apply()
            }
        }

    }
    override fun onResume() {
        super.onResume()
        setup()
    }

    override fun onPause() {
        super.onPause()
    }
    val per=arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION
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
               e.dismiss()
           }
           .create().show()
   }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==req){
            val t=PackageManager.PERMISSION_GRANTED
            if (grantResults[0]==t){
                Toast.makeText(this.requireContext(),"ok", Toast.LENGTH_SHORT).show()
            }else{
                val Snak=AlertDialog.Builder(this.requireContext())
                    .setMessage(this.getString(R.string.faild_GPS_permissions))
                    .setPositiveButton(R.string.ok){e,c->
                        e.dismiss()
                    }.create().show()
            }
        }
    }
    fun checkpermessions():Boolean{
        return this.requireContext().checkSelfPermission(per[0]) == PackageManager.PERMISSION_GRANTED
    }

}