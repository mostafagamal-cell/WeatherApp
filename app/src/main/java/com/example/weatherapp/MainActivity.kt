package com.example.weatherapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
     val db:ActivityMainBinding by lazy {
         ActivityMainBinding.inflate(layoutInflater)
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(db.root)

    }
}