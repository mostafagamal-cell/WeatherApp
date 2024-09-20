package com.example.weatherapp.AppViews

import android.content.Context.MODE_PRIVATE
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
import com.example.weatherapp.myViewModel.ForecastViewModel
import com.example.weatherapp.myViewModel.ForecastViewModelFac

class StartFragment : Fragment() {
     val db: FragmentStartBinding by lazy {
         FragmentStartBinding.inflate(layoutInflater)
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
        return db.root
    }
    private  val TAG = "StartFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}