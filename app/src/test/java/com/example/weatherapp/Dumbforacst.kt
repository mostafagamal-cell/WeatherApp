package com.example.weatherapp

import com.example.weatherapp.forcastmodel.City
import com.example.weatherapp.forcastmodel.Clouds
import com.example.weatherapp.forcastmodel.Coord
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.forcastmodel.Main
import com.example.weatherapp.forcastmodel.Sys
import com.example.weatherapp.forcastmodel.Weather
import com.example.weatherapp.forcastmodel.Wind

val myforecast = Forcast(
    lang = 0,
    cod = "200",
    message = 0,
    cnt = 40,
    city = City(id=7922173, name="العتبة", coord= Coord(lat=30.0444, lon=31.2357), country="EG", population=0, timezone=10800, sunrise=1727495204, sunset=1727538266),
    list = arrayListOf(
        com.example.weatherapp.forcastmodel.List(
        dt = 1727557200,
        main = Main(
            temp = 300.48,
            feelsLike = 301.41,
            tempMin = 299.09,
            tempMax = 300.48,
            pressure = 1012,
            seaLevel = 1012,
            grndLevel = 1006,
            humidity = 57,
            tempKf = 1.39
        ),
        weather = arrayListOf(
            Weather(id = 800, main = "Clear", description = "سماء صافية", icon = "01n")
        ),
        clouds = Clouds(all = 0),
        wind = Wind(speed = 5.19, deg = 359, gust = 6.5),
        visibility = 10000,
        pop = 0.0,
        sys = Sys(pod = "n"),
        dtTxt = "2024-09-28 21:00:00",
        dayname = "Wed",
        time = "2024-09-28",
    ),
        com.example.weatherapp.forcastmodel.List(
            dt = 1727568000,
            main = Main(
                temp = 299.56,
                feelsLike = 299.56,
                tempMin = 297.72,
                tempMax = 299.56,
                pressure = 1012,
                seaLevel = 1012,
                grndLevel = 1005,
                humidity = 59,
                tempKf = 1.84
            ),
            weather = arrayListOf(
                Weather(id = 800, main = "Clear", description = "سماء صافية", icon = "01n")
            ),
            clouds = Clouds(all = 0),
            wind = Wind(speed = 4.17, deg = 346, gust = 5.44),
            visibility = 10000,
            pop = 0.0,
            sys = Sys(pod = "n"),
            dtTxt = "2024-09-29 00:00:00",
            dayname = "Wed",
            time = "2024-09-28",
        )
    )

)