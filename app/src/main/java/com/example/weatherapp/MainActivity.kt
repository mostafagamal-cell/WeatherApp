package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
     val db:ActivityMainBinding by lazy {
         ActivityMainBinding.inflate(layoutInflater)
     }
    companion object{

        fun start(context: Context):LiveData<Boolean>{
            return ConnectionLiveData(context)

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
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


    }
}