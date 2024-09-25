package com.example.weatherapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.weatherapp.AppViews.consts
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.InialBinding
import com.example.weatherapp.weathermodel.cityes
import com.google.gson.Gson
import com.google.gson.JsonArray
import org.json.JSONArray
import java.io.DataInputStream
import java.io.File
import java.io.FileReader

class MainActivity : AppCompatActivity() {
     val db:ActivityMainBinding by lazy {
         ActivityMainBinding.inflate(layoutInflater)
     }
    companion object{
        var allcities:Array<cityes>?=null
        fun start(context: Context):LiveData<Boolean>{
            return ConnectionLiveData(context)

        }
    }
    lateinit var e:MutableLiveData<Boolean>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        db.swipeRefreshLayout.visibility= View.INVISIBLE
        setContentView(db.root)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.baseline_menu_24)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        start()

    }
    private  val TAG = "StartFragment"

    @SuppressLint("NewApi")
    fun  showDailalog(){
        val pref2 = getSharedPreferences(TAG, Context.MODE_PRIVATE)

            val pref =getSharedPreferences(settings, MODE_PRIVATE)
            val e= Dialog(this).apply {
                setContentView(mydb.root)
                mydb.radioGroup2.setOnCheckedChangeListener { radioGroup, i ->
                    if (mydb.GPSrdb.isChecked) {
                        if (!checkpermessions()) {
                            startGPS()
                            return@setOnCheckedChangeListener
                        }
                        pref.edit().putInt(mode, consts.GPS.ordinal).apply()
                    } else {
                        pref.edit().putInt(mode, consts.Map.ordinal).apply()
                    }
                }
                mydb.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
                    if (mydb.radioButton.isChecked) {
                        pref.edit().putInt(language, consts.en.ordinal).apply()
                    } else {
                        pref.edit().putInt(language, consts.ar.ordinal).apply()
                    }
                }
                mydb.ok.setOnClickListener {
                    if (pref.contains(mode) && pref.contains(language)) {
                        pref2.edit().putBoolean("first", false).apply()
                        dismiss()
                        e.value=false
                    }
                }
                setCancelable(false)
                mydb.button3.setOnClickListener {
                    finish()
                }

            }
            e.create()
            e.show()

    }
    fun checkpermessions():Boolean{
        return this.checkSelfPermission(per[0]) == PackageManager.PERMISSION_GRANTED
    }
    fun start(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(db.navigationView, navController)
        val pref2 = getSharedPreferences(TAG, Context.MODE_PRIVATE)
        e= MutableLiveData<Boolean>(true)
        e.value=pref2.getBoolean("first", true)
        e.observe(this) {
            if (it==false) {
                             var id = navController.currentDestination!!.id
                navController.popBackStack(id, true)
                navController.navigate(id)
                db.swipeRefreshLayout.visibility= View.VISIBLE

                navController.addOnDestinationChangedListener { controller, destination, arguments ->
                    db.swipeRefreshLayout.isEnabled = destination.id != R.id.mapFragment
                }
                db.swipeRefreshLayout.setOnRefreshListener {
                    if (navController.currentDestination?.id != null) {
                        id = navController.currentDestination!!.id
                        navController.popBackStack(id, true)
                        navController.navigate(id)
                    }
                    db.swipeRefreshLayout.isRefreshing = false
                }
            }else{
                showDailalog()
            }
        }
    }
    val per=arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION
        ,android.Manifest.permission.ACCESS_COARSE_LOCATION
        ,android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ,android.Manifest.permission.SET_ALARM
        ,android.Manifest.permission.SCHEDULE_EXACT_ALARM
        ,android.Manifest.permission.INTERNET
        ,android.Manifest.permission.POST_NOTIFICATIONS
    )
    val mydb: InialBinding by lazy {
        InialBinding.inflate(layoutInflater)
    }
    private fun startGPS(){
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(this.getString(R.string.request_GPS_permissions))
            .setMessage(this.getString(R.string.please_Give_GPS_permissions))
            .setPositiveButton(this.getString(R.string.ok)) { _, _ ->
                requestPermessions()
            }
            .setNegativeButton(this.getString(R.string.cancel)) { e, c ->
                mydb.Maprdb.isChecked=true
                e.dismiss()
            }.create().show()
    }
    fun requestPermessions(){
        requestPermissions(per,req)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==req){
            val t= PackageManager.PERMISSION_GRANTED
            if (grantResults[0]==t){
                mydb.GPSrdb.isChecked=true
               getSharedPreferences(settings, MODE_PRIVATE).edit().putInt(mode,consts.GPS.ordinal).apply()
            }else{
                mydb.Maprdb.isChecked=true
                getSharedPreferences(settings, MODE_PRIVATE).edit().putInt(mode,consts.Map.ordinal).apply()
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setMessage(this.getString(R.string.faild_GPS_permissions))
                    .setPositiveButton(R.string.ok){e,c->
                        e.dismiss()
                    }.create().show()
            }
        }
    }
    val req=101
}