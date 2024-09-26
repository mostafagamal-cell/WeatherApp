package com.example.weatherapp.AppViews

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.MyNetwork.API
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentAlarmsBinding
import com.example.weatherapp.databinding.FragmentFavBinding
import com.example.weatherapp.favitem
import com.example.weatherapp.forcastmodel.Favorites
import com.example.weatherapp.lat
import com.example.weatherapp.longite
import com.example.weatherapp.map
import com.example.weatherapp.myViewModel.ForecastViewModel
import com.example.weatherapp.myViewModel.ForecastViewModelFac
import com.example.weatherapp.myViewModel.State
import com.google.gson.Gson
import kotlinx.coroutines.launch

class FavFragment : Fragment() {
     val db: FragmentFavBinding by lazy {
      return@lazy FragmentFavBinding.inflate(layoutInflater)
    }
    val viewmodel: ForecastViewModel by lazy {
        val fac= ForecastViewModelFac(
            LocalDataSource(ForecastDataBase.getDatabase(requireContext()).yourDao()),
            RemoteDataSource(API))
        return@lazy ViewModelProvider(this,fac)[ForecastViewModel::class.java]
    }
    var selected=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return db.root
    }
    val adapter=FavAdp({ viewmodel.deleteFav(it) }, {
        val item=it
        val gson= Gson().toJson(it)
        requireActivity().getSharedPreferences(favitem, MODE_PRIVATE).edit().putString(favitem,gson).apply()
        findNavController().navigate(R.id.itemFav)
    })
    override fun onViewCreated(e: View, savedInstanceState: Bundle?) {
        super.onViewCreated(e, savedInstanceState)
        db.floatingActionButton.setOnClickListener {
            selected=true
            findNavController().navigate(R.id.mapFragment)
        }
        viewmodel.getFavs()
        db.fav.adapter=adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewmodel.favorete.collect{
                    if(it is State.Success) {
                        Log.i("FavFragment", "onViewCreated: ${it.data}")
                        adapter.submitList(it.data as List<Favorites>)}
                    else if(it is State.Error)  Toast.makeText(requireContext(), it.message.message, Toast.LENGTH_SHORT).show()
                    else if(it is State.Loading) Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (selected){
            val lat = requireActivity().getSharedPreferences(map, MODE_PRIVATE).getFloat(lat, 0f).toDouble()
            val lon = requireActivity().getSharedPreferences(map, MODE_PRIVATE).getFloat(longite, 0f).toDouble()
            val e = requireActivity().getSharedPreferences(map, MODE_PRIVATE).getString("name","")
            viewmodel.addFav(e!!,lat,lon)
            selected=false
            requireActivity().getSharedPreferences(map, MODE_PRIVATE).edit().clear().apply()
        }
    }
}