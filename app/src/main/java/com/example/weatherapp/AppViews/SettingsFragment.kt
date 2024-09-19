package com.example.weatherapp.AppViews

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val mode = pref.getInt(mode, 0)
        val unite = pref.getInt(units, 0)
        val language = pref.getInt(language, 0)
        val mynotification = pref.getInt(notification, 0)
        if (mynotification == 1) {
            db.enablenf.isChecked = true
        } else {
            db.disablenf.isChecked = true
        }
        if (mode == 1) {
            db.GPSrdb.isChecked = true
        } else {
            db.Map.isChecked = true
        }
        if (language == 1) {
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
        db.notification.setOnCheckedChangeListener { e, c ->
            if (db.enablenf.isChecked) {
                pref.edit().putInt(notification, 1).apply()
            } else {
                pref.edit().putInt(notification, 0).apply()
            }
        }

        db.enablenf.setOnCheckedChangeListener { e, c ->
            if (db.enablenf.isChecked) {
                pref.edit().putInt(notification, 1).apply()
            } else {
                pref.edit().putInt(notification, 0).apply()
            }
        }
        db.SpeenUnits.setOnCheckedChangeListener { e, c ->
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
   fun requestPermessions(){
       requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION
       ,android.Manifest.permission.ACCESS_COARSE_LOCATION
       ,android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
       ,android.Manifest.permission.SET_ALARM
        ,android.Manifest.permission.SCHEDULE_EXACT_ALARM
        ,android.Manifest.permission.INTERNET
        ,android.Manifest.permission.POST_NOTIFICATIONS
       ),req)
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
            if (grantResults[0]==1&&grantResults[1]==1&&grantResults[2]==1&&grantResults[3]==1&&grantResults[4]==1&&grantResults[5]==1&&grantResults[6]==1){

            }else{
                val Snak=AlertDialog.Builder(this.requireContext())
                    .setMessage(this.getString(R.string.faild_GPS_permissions))
                    .setPositiveButton(R.string.ok){e,c->
                        e.dismiss()
                    }.create().show()
            }
        }
    }

}