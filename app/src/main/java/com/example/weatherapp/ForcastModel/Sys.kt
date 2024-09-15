package com.example.weatherapp.ForcastModel

import com.google.gson.annotations.SerializedName


data class Sys (
  @SerializedName("pod" ) var pod : String? = null

)