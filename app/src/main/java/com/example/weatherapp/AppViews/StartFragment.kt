package com.example.weatherapp.AppViews

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.forcastmodel.City
import com.example.weatherapp.forcastmodel.Coord
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.MyNetwork.API
import com.example.weatherapp.R
import com.example.weatherapp.States
import com.example.weatherapp.current
import com.example.weatherapp.databinding.FragmentStartBinding
import com.example.weatherapp.databinding.InialBinding
import com.example.weatherapp.language
import com.example.weatherapp.lat
import com.example.weatherapp.longite
import com.example.weatherapp.map
import com.example.weatherapp.mode
import com.example.weatherapp.myViewModel.ForecastViewModel
import com.example.weatherapp.myViewModel.ForecastViewModelFac
import com.example.weatherapp.pickec
import com.example.weatherapp.settings
import com.example.weatherapp.weathermodel.Clouds
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import com.example.weatherapp.weathermodel.Main
import com.example.weatherapp.weathermodel.Sys
import com.example.weatherapp.weathermodel.Weather
import com.example.weatherapp.weathermodel.Wind
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class StartFragment : Fragment() {

    val db: FragmentStartBinding by lazy {
         FragmentStartBinding.inflate(layoutInflater)
     }
    val mydb: InialBinding by lazy {
        InialBinding.inflate(layoutInflater)
    }
     val viewModel: ForecastViewModel by lazy {
         ViewModelProvider(this,ForecastViewModelFac(LocalDataSource(ForecastDataBase.getDatabase(requireContext()).yourDao()),
             RemoteDataSource(API)
         ))[ForecastViewModel::class.java]
     }
    var select=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        showDailalog()
        return db.root
    }
    private  val TAG = "StartFragment"
         override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          super.onViewCreated(view, savedInstanceState)
             viewModel.state.observe(viewLifecycleOwner){
                 when(it){
                      States.Loading->{
                          db.viewModel=createTempWeather()
                      }
                      States.Success->{
                          val e=viewModel.weather.value
                         db.viewModel=e
                      }
                     States.Error->{
                         Toast.makeText(this.requireContext(),this.getString(R.string.ok),Toast.LENGTH_LONG).show()
                     }
                     else->{
                         db.viewModel=createTempWeather()
                     }
                 }
             }
         }
    fun createTempForecast():Forcast{
        return Forcast(
            city =City(-1,"---",Coord(1.0000,0.0),"----")
            , list = arrayListOf()
            , cnt = 0
            , message = 0
            , cod = ""
            , lang = requireActivity().getSharedPreferences(settings, MODE_PRIVATE).getInt(language,consts.ar.ordinal)
        )
    }




    private fun startGPS(){
        androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
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
                requireActivity().getSharedPreferences(settings, MODE_PRIVATE).edit().putInt(mode,consts.GPS.ordinal).apply()
            }else{
                mydb.Maprdb.isChecked=true
                requireActivity().getSharedPreferences(settings, MODE_PRIVATE).edit().putInt(mode,consts.Map.ordinal).apply()
                androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
                    .setMessage(this.getString(R.string.faild_GPS_permissions))
                    .setPositiveButton(R.string.ok){e,c->
                        e.dismiss()
                    }.create().show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (requireActivity().getSharedPreferences(settings, MODE_PRIVATE).getInt(mode,consts.Map.ordinal)==consts.GPS.ordinal){
            db.gotomap.visibility=View.INVISIBLE
         if (!checkpermessions()){
            Toast.makeText(this.requireContext(),this.getString(R.string.please_Give_GPS_permissions),Toast.LENGTH_LONG).show()
         }
            val manager=requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var gps=false
            var network=false
            var isgpsav=true
            var isnetworkav=true
            try {
                gps=manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            }catch (e:Exception){
               isgpsav=false
            }
            try {
                network=manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            }catch (e:Exception){
               isnetworkav=false
            }
            if (isgpsav||isnetworkav){
            if (!gps&&!network){
                androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
                    .setTitle(this.getString(R.string.GPS))
                    .setMessage(this.getString(R.string.open_location_settings))
                    .setPositiveButton(R.string.ok){e,c->
                        val intetn=Intent(ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intetn)
                    }
                    .setNegativeButton(R.string.cancel){e,c->
                        e.dismiss()
                    }.show()
                }
                if (checkpermessions()) {
                    fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity());
                    fusedClient!!.lastLocation.addOnSuccessListener {
                        if (it != null) {
                            viewModel.getWeather(
                                it.latitude,
                                it.longitude,
                                requireActivity().getSharedPreferences(settings, MODE_PRIVATE)
                                    .getInt(language, consts.ar.ordinal)
                            )
                         }
                        }
                }
            }else{
                Toast.makeText(this.requireContext(),this.getString(R.string.no_loc_av),Toast.LENGTH_LONG).show()
            }
        }else{
            db.gotomap.visibility=View.VISIBLE
            var late=requireActivity().getSharedPreferences(map, MODE_PRIVATE).getFloat(lat, 0.0F)
            var lon=requireActivity().getSharedPreferences(map, MODE_PRIVATE).getFloat(longite, 0.0F)

            db.gotomap.setOnClickListener {
                findNavController().navigate(R.id.mapFragment)
                select=true
            }
                val e= requireActivity().getSharedPreferences(TAG, MODE_PRIVATE)
                late=requireActivity().getSharedPreferences(map, MODE_PRIVATE).getFloat(lat,e.getFloat(lat,0.0F))
                lon=requireActivity().getSharedPreferences(map, MODE_PRIVATE).getFloat(longite,e.getFloat(longite,0.0F))
                val ee=e.edit()
                ee.putFloat(lat,late)
                ee.putFloat(longite,lon)
                ee.apply()
                viewModel.getWeather(late.toDouble(),lon.toDouble(),requireActivity().getSharedPreferences(settings, MODE_PRIVATE).getInt(language,consts.ar.ordinal))
        }
    }
    private  var fusedClient : FusedLocationProviderClient?=null

    override fun onPause() {
        super.onPause()
        fusedClient=null
    }


    val per=arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION
        ,android.Manifest.permission.ACCESS_COARSE_LOCATION
        ,android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ,android.Manifest.permission.SET_ALARM
        ,android.Manifest.permission.SCHEDULE_EXACT_ALARM
        ,android.Manifest.permission.INTERNET
        ,android.Manifest.permission.POST_NOTIFICATIONS
    )
    val req=101
    fun checkpermessions():Boolean{
        return this.requireContext().checkSelfPermission(per[0]) == PackageManager.PERMISSION_GRANTED
    }
    fun  showDailalog(){
        val pref2 = requireActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE)
        if (pref2.getBoolean("first", true)) {
            val pref = requireActivity().getSharedPreferences(settings, MODE_PRIVATE)
            db.scrollView.visibility=View.INVISIBLE
            Dialog(requireContext()).apply {
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
                        db.scrollView.visibility=View.VISIBLE
                        pref2.edit().putBoolean("first", false).apply()
                        dismiss()
                    }
                }
                setCancelable(false)
                mydb.button3.setOnClickListener {
                    requireActivity().finish()
                }
                create()
            }.show()
        }
    }
    fun createTempWeather():ExampleJson2KtKotlin{
        return ExampleJson2KtKotlin(
            name = "---", base = "-----", clouds = Clouds(0), cod = 0,
            wind = Wind(
                deg = 0,
                speed = 0.0
            ),
            weather = arrayListOf(Weather(0,"---","---","---")),
            dt = 0,
            id = 0,
            isFavorite = false,
            main = Main(0.0 , 0.0, 0.0, 0.0, 0, 0, 0, 0), sys = Sys(0, 0, "", 0, 0),
            visibility = 0, timezone = 0, coord = com.example.weatherapp.weathermodel.Coord(0.0,0.0), language = requireActivity().getSharedPreferences(settings, MODE_PRIVATE).getInt(language,consts.ar.ordinal)
        )
    }
}
