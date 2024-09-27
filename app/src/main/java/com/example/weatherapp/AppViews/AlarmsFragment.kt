package com.example.weatherapp.AppViews

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.DataSource.LocalDataSource
import com.example.weatherapp.DataSource.RemoteDataSource
import com.example.weatherapp.ForecastDatabase.ForecastDataBase
import com.example.weatherapp.MyNetwork.API
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentFavBinding
import com.example.weatherapp.databinding.SelecttimedialogBinding
import com.example.weatherapp.lat
import com.example.weatherapp.longite
import com.example.weatherapp.map
import com.example.weatherapp.myViewModel.ForecastViewModel
import com.example.weatherapp.myViewModel.ForecastViewModelFac
import java.text.SimpleDateFormat
import java.util.Calendar


class AlarmsFragment : Fragment() {
     val db:FragmentFavBinding by lazy {
         return@lazy FragmentFavBinding.inflate(layoutInflater)
     }
    var selected=false
    val adapter=ItemFav()
    val viewmodel: ForecastViewModel by lazy {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db.floatingActionButton.setOnClickListener {
            val binding= SelecttimedialogBinding.inflate(layoutInflater)
            binding.button2.setOnClickListener {
                
            }
            Dialog(this.requireContext()).apply {
                setContentView(db.root)
                show()
            }
            showDateTimePicker()
        }

    }
    override fun onResume() {
        super.onResume()
        if (selected){
            val lat = requireActivity().getSharedPreferences(map, MODE_PRIVATE).getFloat(lat, 0f).toDouble()
            val lon = requireActivity().getSharedPreferences(map, MODE_PRIVATE).getFloat(longite, 0f).toDouble()
            val e = requireActivity().getSharedPreferences(map, MODE_PRIVATE).getString("name","")
            selected=false
            requireActivity().getSharedPreferences(map, MODE_PRIVATE).edit().clear().apply()
        }
    }
    private fun showDateTimePicker() {
        // Get Current Date and Time
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // When date is selected, show the TimePickerDialog
                showTimePicker(selectedYear, selectedMonth, selectedDay)
            },
            year, month, day
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    private fun showTimePicker(year: Int, month: Int, day: Int) {
        // Get Current Time
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                // When time is selected, get the selected date and time in milliseconds
                val selectedDateTime = Calendar.getInstance()
                selectedDateTime.set(year, month, day, selectedHour, selectedMinute, 0)

                // Convert selected date and time to milliseconds
                val timeInMillis = selectedDateTime.timeInMillis
                val format1: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
                val formatter = format1.format(calendar.time)

                // Do something with timeInMillis (e.g., print or use it in your app)
               Log.i("sssssssssiizzzz","Success  ${timeInMillis}  ${formatter}")
            },
            hour, minute, true
        )

        // Show the TimePickerDialog
        timePickerDialog.show()
    }
}

