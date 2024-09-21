package com.example.weatherapp.AppViews

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.MyNetwork.API
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentStartBinding
import com.example.weatherapp.databinding.InialBinding
import com.example.weatherapp.language
import com.example.weatherapp.mode
import com.example.weatherapp.myViewModel.ForecastViewModel
import com.example.weatherapp.myViewModel.ForecastViewModelFac
import com.example.weatherapp.settings

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val pref = requireActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE)
        if (requireActivity().getSharedPreferences(TAG, MODE_PRIVATE).getBoolean("first", true)) {
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
                            pref.edit().putBoolean("first", false).apply()
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
        return db.root
    }
    private  val TAG = "StartFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
          }
    fun startGPS(){
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
            }else{
                mydb.Maprdb.isChecked=true
               androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
                    .setMessage(this.getString(R.string.faild_GPS_permissions))
                    .setPositiveButton(R.string.ok){e,c->
                        e.dismiss()
                    }.create().show()
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
    val req=101
    fun checkpermessions():Boolean{
        return this.requireContext().checkSelfPermission(per[0]) == PackageManager.PERMISSION_GRANTED
    }
}
