package com.example.weatherapp.AppViews

import android.content.Context.MODE_PRIVATE
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.MyNetwork.API
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentStartBinding
import com.example.weatherapp.favitem
import com.example.weatherapp.forcastmodel.Favorites
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.language
import com.example.weatherapp.myViewModel.ForecastViewModel
import com.example.weatherapp.myViewModel.ForecastViewModelFac
import com.example.weatherapp.myViewModel.State
import com.example.weatherapp.settings
import com.example.weatherapp.weathermodel.Clouds
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import com.example.weatherapp.weathermodel.Main
import com.example.weatherapp.weathermodel.Sys
import com.example.weatherapp.weathermodel.Weather
import com.example.weatherapp.weathermodel.Wind
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ItemFav : Fragment() {
    val db: FragmentStartBinding by lazy {
       return@lazy FragmentStartBinding.inflate(layoutInflater)
    }
    val viewModel: ForecastViewModel by lazy {
        val fac= ForecastViewModelFac(
            LocalDataSource(ForecastDataBase.getDatabase(requireContext()).yourDao()),
            RemoteDataSource(API)
        )
        return@lazy ViewModelProvider(this,fac)[ForecastViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return db.root
    }
    var adpt=TodayAdapter()
    var adpt2=WeekAdpter()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args= Gson().fromJson(requireActivity().getSharedPreferences(favitem,MODE_PRIVATE).getString(favitem,""), Favorites::class.java)
        viewModel.getWeather(args.lat,args.lon,requireActivity().getSharedPreferences(
            settings, MODE_PRIVATE
        ).getInt(language,consts.ar.ordinal))
        viewModel.getForecasts(args.lat,args.lon,requireActivity().getSharedPreferences(
            settings, MODE_PRIVATE
        ).getInt(language,consts.ar.ordinal))
        db.recyclerView.adapter=adpt2
        db.recyclerView3.adapter=adpt
        db.gotomap.visibility=View.INVISIBLE
        db.viewModel=createTempWeather()
        coolect()

    }
    fun coolect(){

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.weather.collect{
                    if (it is State.Success){
                        val t= it.data as ExampleJson2KtKotlin
                        db.viewModel=(t)
                        db.weatherprograss.visibility=View.INVISIBLE
                        db.weatherstate.visibility=View.VISIBLE
                        db.stateprograss.visibility=View.INVISIBLE
                        db.details.visibility=View.VISIBLE
                        db.invalidateAll()
                    }
                    if (it is State.Error){

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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.hours.collect{

                    if (it is State.Success){
                        val  e =it.data as Forcast
                        db.recyclerView3.visibility=View.VISIBLE
                        db.timeprograss.visibility=View.INVISIBLE
                        adpt.submitList(e.list)
                    }
                    if (it is State.Error){

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
                    }
                    if (it is State.Error){

                    }
                    if (it is State.Loading){
                        db.recyclerView.visibility=View.INVISIBLE
                        db.daysprograss.visibility=View.VISIBLE
                    }
                }
            }
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
            visibility = 0, timezone = 0, coord = com.example.weatherapp.weathermodel.Coord(0.0,0.0), language = requireActivity().getSharedPreferences(
                settings, MODE_PRIVATE
            ).getInt(language,consts.ar.ordinal)
        )
    }
}