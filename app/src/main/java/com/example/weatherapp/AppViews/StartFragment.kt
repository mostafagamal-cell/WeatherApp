package com.example.weatherapp.AppViews

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.CallLog.Locations
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.forcastmodel.City
import com.example.weatherapp.forcastmodel.Coord
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.MainActivity
import com.example.weatherapp.MyNetwork.API
import com.example.weatherapp.R
import com.example.weatherapp.States
import com.example.weatherapp.current
import com.example.weatherapp.databinding.FragmentStartBinding
import com.example.weatherapp.databinding.InialBinding
import com.example.weatherapp.forcastmodel.List
import com.example.weatherapp.language
import com.example.weatherapp.lat
import com.example.weatherapp.longite
import com.example.weatherapp.map
import com.example.weatherapp.mode
import com.example.weatherapp.myViewModel.ForecastViewModel
import com.example.weatherapp.myViewModel.ForecastViewModelFac
import com.example.weatherapp.myViewModel.State
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
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartFragment : Fragment() {

    val db: FragmentStartBinding by lazy {
         FragmentStartBinding.inflate(layoutInflater)
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
        return db.root
    }
    private  val TAG = "StartFragment"


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

                requireActivity().getSharedPreferences(settings, MODE_PRIVATE).edit().putInt(mode,consts.GPS.ordinal).apply()
            }else{
                requireActivity().getSharedPreferences(settings, MODE_PRIVATE).edit().putInt(mode,consts.Map.ordinal).apply()
                androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
                    .setMessage(this.getString(R.string.faild_GPS_permissions))
                    .setPositiveButton(R.string.ok){e,c->
                        e.dismiss()
                    }.create().show()
            }
        }
    }
     lateinit var late:MutableLiveData<Float>
     lateinit var lon:MutableLiveData<Float>
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db.viewModel=createTempWeather()
        late=MutableLiveData(requireActivity().getSharedPreferences(TAG, MODE_PRIVATE).getFloat(lat, 0.0F))
        lon=MutableLiveData(requireActivity().getSharedPreferences(TAG, MODE_PRIVATE).getFloat(longite, 0.0F))
        MainActivity.Companion.start(requireContext()).observe(viewLifecycleOwner){e->
            if (e==true){
                if (requireActivity().getSharedPreferences(settings, MODE_PRIVATE).getInt(mode,consts.Map.ordinal)==consts.GPS.ordinal){

                    if (checkpermessions())
                    {
                        getDeviceLocation()
                    }else{
                        requestPermessions()
                    }
                }else{
                   reqeustToGetData()
                }
            }else{
                db.stateprograss.visibility=View.INVISIBLE
                db.weatherprograss.visibility=View.INVISIBLE
                db.weatherstate.visibility=View.VISIBLE
                db.weatherprograss.visibility=View.INVISIBLE
            }
        }
        db.recyclerView3.adapter=adpt
        if (requireActivity().getSharedPreferences(settings, MODE_PRIVATE).getInt(mode,consts.Map.ordinal)==consts.Map.ordinal){
            db.gotomap.visibility=View.VISIBLE
            db.gotomap.setOnClickListener {
                findNavController().navigate(R.id.mapFragment)
                select=true
            }
            Log.i("eeeqqqqqqqqqqqeeeeeeeefff"," before map  $late $lon")
            if (select) {
                select=false
                val e = requireActivity().getSharedPreferences(TAG, MODE_PRIVATE)
                late.value = requireActivity().getSharedPreferences(map, MODE_PRIVATE).getFloat(lat, e.getFloat(lat,0F))
                lon.value = requireActivity().getSharedPreferences(map, MODE_PRIVATE).getFloat(longite, e.getFloat(longite,  0F))
                val ee = e.edit()
                ee.putFloat(lat, late.value!!)
                ee.putFloat(longite, lon.value!!)
                ee.apply()
                reqeustToGetData()
            }
            Log.i("eeeqqqqqqqqqqqeeeeeeeefff"," after map  $late $lon")
            requireActivity().getSharedPreferences(map, MODE_PRIVATE).edit().clear().apply()
        }
        coolect()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission", "RepeatOnLifecycleWrongUsage")
    override fun onResume() {
        super.onResume()
        if (requireActivity().getSharedPreferences(settings, MODE_PRIVATE).getInt(mode,consts.Map.ordinal)==consts.GPS.ordinal){
            Log.i("sdasdasdasdasdas","hhhhhhhhhhhhhhhhhhhhh")
            late=MutableLiveData(requireActivity().getSharedPreferences(TAG, MODE_PRIVATE).getFloat(lat, 0.0F))
            lon=MutableLiveData(requireActivity().getSharedPreferences(TAG, MODE_PRIVATE).getFloat(longite, 0.0F))
            db.gotomap.visibility=View.INVISIBLE
        if (!checkpermessions()){
            AlertDialog.Builder(this.requireContext())
                .setTitle(this.getString(R.string.request_GPS_permissions)).setMessage(R.string.please_Give_GPS_permissions).setPositiveButton(R.string.ok){e,c->
                    requestPermessions()
                }
        }
            getDeviceLocation()

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
                        reqeustToGetData()
                        e.dismiss()
                    }
                    .setNegativeButton(R.string.cancel){e,c->
                        late=MutableLiveData(requireActivity().getSharedPreferences(TAG, MODE_PRIVATE).getFloat(lat, 0.0F))
                        lon=MutableLiveData(requireActivity().getSharedPreferences(TAG, MODE_PRIVATE).getFloat(longite, 0.0F))
                        reqeustToGetData()
                        e.dismiss()
                    }.show()
            }
        }else{
            Toast.makeText(this.requireContext(),this.getString(R.string.no_loc_av),Toast.LENGTH_LONG).show()
         }
         }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun reqeustToGetData() {
        viewModel.getWeather(
            late.value!!.toDouble(), lon.value!!.toDouble(),
            requireActivity().getSharedPreferences(settings, MODE_PRIVATE)
                .getInt(language, consts.ar.ordinal)
        )
        viewModel.getForecasts(
            late.value!!.toDouble(),
            lon.value!!.toDouble(),
            requireActivity().getSharedPreferences(settings, MODE_PRIVATE)
                .getInt(language, consts.ar.ordinal)
        )
    }


    fun coolect(){
        db.recyclerView.adapter=adpt2
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.weather.collect{
                    Log.i("1111111111111111111","onViewCreated: ${it}")
                    if (it is State.Success){
                        val t= it.data as ExampleJson2KtKotlin
                        Log.i("1111111111111111111","onViewCreated: ${t.name}")

                        requireActivity().getSharedPreferences(TAG, MODE_PRIVATE).edit().putString("name",t.name).apply()
                        db.viewModel=(t)
                        db.weatherprograss.visibility=View.INVISIBLE
                        db.weatherstate.visibility=View.VISIBLE
                        db.stateprograss.visibility=View.INVISIBLE
                        db.details.visibility=View.VISIBLE
                        db.invalidateAll()
                    }
                    if (it is State.Error){
                        db.viewModel=createTempWeather()
                        db.weatherprograss.visibility=View.INVISIBLE
                        db.weatherstate.visibility=View.VISIBLE
                        db.stateprograss.visibility=View.INVISIBLE
                        db.details.visibility=View.VISIBLE


                    }
                    if (it is State.Loading){
                    db.weatherstate.visibility=View.INVISIBLE
                    db.weatherprograss.visibility=View.VISIBLE
                    db.stateprograss.visibility=View.VISIBLE
                    db.details.visibility=View.INVISIBLE


                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner. repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.hours.collect{

                    if (it is State.Success){
                        val  e =it.data as Forcast
                        adpt.submitList(e.list)
                        db.recyclerView3.visibility=View.VISIBLE
                        db.timeprograss.visibility=View.INVISIBLE
                        db.invalidateAll()
                    }
                    if (it is State.Error){
                        db.viewModel=createTempWeather()
                        db.recyclerView3.visibility=View.VISIBLE
                        db.timeprograss.visibility=View.INVISIBLE
                    }
                    if (it is State.Loading){
                            db.recyclerView3.visibility=View.INVISIBLE
                            db.timeprograss.visibility=View.VISIBLE

                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.day.collect{
                    if (it is State.Success){
                        val  data=it.data as Forcast
                        adpt2.submitList(data.list)
                        db.recyclerView.visibility=View.VISIBLE
                        db.daysprograss.visibility=View.INVISIBLE
                        db.invalidateAll()
                    }
                    if (it is State.Error){
                        db.viewModel=createTempWeather()
                        db.recyclerView.visibility=View.VISIBLE
                        db.daysprograss.visibility=View.INVISIBLE
                    }
                    if (it is State.Loading){
                        db.recyclerView.visibility=View.INVISIBLE
                        db.daysprograss.visibility=View.VISIBLE
                    }
                }
            }
        }
    }

    private  var fusedClient : FusedLocationProviderClient?=null
    override fun onDestroy() {
        super.onDestroy()
        fusedClient=null
    }

     var adpt=TodayAdapter()
    var adpt2=WeekAdpter()
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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleNewLocation() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            val result = fusedLocationProviderClient.getCurrentLocation(
                priority,
                CancellationTokenSource().token,
            )
            result.let { fetchedLocation ->
                withContext(Dispatchers.Main) {
                    if (fetchedLocation.isSuccessful){
                    Log.i("lllllllllllllllllllllllllllllllllll","${fetchedLocation.result.latitude} ${fetchedLocation.result.longitude}")
                    requireActivity().getSharedPreferences(TAG, MODE_PRIVATE).edit()
                        .putFloat(lat, fetchedLocation.result.latitude.toFloat())
                        .putFloat(longite, fetchedLocation.result.longitude.toFloat())
                        .apply()
                    late.value=fetchedLocation.result.latitude.toFloat()
                    lon.value=(fetchedLocation.result.longitude.toFloat())
                    reqeustToGetData()
                }
            }}
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDeviceLocation() {
        Log.i("lllllllllllllllllllllllllllllllllll","get device location")
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val locationResult = fusedLocationProviderClient.lastLocation
        locationResult.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // i check for the result here, if null it means the location object is null
                if (task.result == null) {
                    // so we need to find a different way to force this retrieval
                    handleNewLocation()
                    return@addOnCompleteListener
                }
                Log.i("lllllllllllllllllllllllllllllllllll","${task.result.latitude} ${task.result.longitude}")
                requireActivity().getSharedPreferences(TAG, MODE_PRIVATE).edit()
                    .putFloat(lat, task.result.latitude.toFloat())
                    .putFloat(longite, task.result.longitude.toFloat())
                    .apply()
                late.value=task.result.latitude.toFloat()
                lon.value=task.result.longitude.toFloat()
                reqeustToGetData()
            } else {
                handleNewLocation()
            }
        }
}
}
