package com.example.weatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
     val db:ActivityMainBinding by lazy {
         ActivityMainBinding.inflate(layoutInflater)
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(db.root)

        val nav =  supportFragmentManager.findFragmentById(db.fragmentContainerView.id) as NavHostFragment
        val navController = nav.navController
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            db.swipeRefreshLayout.isEnabled = destination.id != R.id.mapFragment
        }
        db.swipeRefreshLayout.setOnRefreshListener {
            if (navController.currentDestination?.id!=null ) {
                val id = navController.currentDestination!!.id
                navController.popBackStack(id, true)
                navController.navigate(id)
            }
            db.swipeRefreshLayout.isRefreshing=false
        }
        val connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) {
            Toast.makeText(this, "internet $it", Toast.LENGTH_SHORT).show()
        }
    }
}