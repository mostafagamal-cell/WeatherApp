package com.example.weatherapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.weatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
     val db:ActivityMainBinding by lazy {
         ActivityMainBinding.inflate(layoutInflater)
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(db.root)
        db.swipeRefreshLayout.setOnRefreshListener {
            val navController = findNavController(R.id.fragmentContainerView)
           val id= navController.currentDestination!!.id
            navController.popBackStack(id, true)
            navController.navigate(id)
            db.swipeRefreshLayout.isRefreshing=false
        }
    }
}