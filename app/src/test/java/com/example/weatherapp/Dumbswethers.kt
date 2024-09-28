package com.example.weatherapp

import com.example.weatherapp.AppViews.consts
import com.example.weatherapp.alerts.MyAlerts
import com.example.weatherapp.forcastmodel.Favorites
import com.example.weatherapp.weathermodel.Clouds
import com.example.weatherapp.weathermodel.Coord
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import com.example.weatherapp.weathermodel.Main
import com.example.weatherapp.weathermodel.Sys
import com.example.weatherapp.weathermodel.Weather
import com.example.weatherapp.weathermodel.Wind

val dumbData = arrayListOf(
    ExampleJson2KtKotlin(
    coord = Coord(lon = 31.2357, lat = 30.0444),
    weather = arrayListOf(Weather(id = 800, main = "Clear", description = "سماء صافية", icon = "01n"),),
    base = "stations",
    main = Main(temp = 301.48, feelsLike = 302.08, tempMin = 301.07, tempMax = 301.48, pressure = 1012, humidity = 51, seaLevel = 1012, grndLevel = 1006),
    visibility = 10000,
    wind = Wind(speed = 5.66, deg = 10),
    clouds = Clouds(all = 0),
    dt = 1727551241,
    sys = Sys(type = 1, id = 2514, country = "EG", sunrise = 1727495204, sunset = 1727538266),
    timezone = 10800,
    id = 7922173,
    language = 0,
    name = "العتبة",
    cod = 200,
    isFavorite = false,
    lat = 30.0444,
    lon = 31.2357
), ExampleJson2KtKotlin(
    coord = Coord(lon = 37.6173, lat = 55.7558),
    weather = arrayListOf(Weather(id = 801, main = "Clouds", description = "few clouds", icon = "02d")),
    base = "stations",
    main = Main(temp = 285.32, feelsLike = 283.57, tempMin = 284.15, tempMax = 286.15, pressure = 1015, humidity = 71, seaLevel = 1015, grndLevel = 1008),
    visibility = 10000,
    wind = Wind(speed = 3.09, deg = 290),
    clouds = Clouds(all = 20),
    dt = 1727551241,
    sys = Sys(type = 1, id = 1234, country = "RU", sunrise = 1727492404, sunset = 1727535204),
    timezone = 10800,
    id = 524901,
    language = 0,
    name = "Moscow",
    cod = 200,
    isFavorite = false,
    lat = 55.7558,
    lon = 37.6173
), ExampleJson2KtKotlin(
    coord = Coord(lon = 21.0122, lat = 52.2297),
    weather = arrayListOf(Weather(id = 802, main = "Clouds", description = "scattered clouds", icon = "03d")),
    base = "stations",
    main = Main(temp = 289.13, feelsLike = 288.45, tempMin = 288.15, tempMax = 290.15, pressure = 1018, humidity = 65, seaLevel = 1018, grndLevel = 1013),
    visibility = 10000,
    wind = Wind(speed = 4.12, deg = 270),
    clouds = Clouds(all = 40),
    dt = 1727551241,
    sys = Sys(type = 1, id = 2516, country = "PL", sunrise = 1727495204, sunset = 1727538266),
    timezone = 7200,
    id = 756135,
    language = 0,
    name = "Warsaw",
    cod = 200,
    isFavorite = false,
    lat = 52.2297,
    lon = 21.0122
)
    , ExampleJson2KtKotlin(
        coord = Coord(lon = -77.0369, lat = 38.9072),
        weather = arrayListOf(Weather(id = 803, main = "Clouds", description = "broken clouds", icon = "04d")),
        base = "stations",
        main = Main(temp = 298.23, feelsLike = 299.18, tempMin = 297.15, tempMax = 299.15, pressure = 1010, humidity = 74, seaLevel = 1010, grndLevel = 1007),
        visibility = 10000,
        wind = Wind(speed = 3.6, deg = 210),
        clouds = Clouds(all = 75),
        dt = 1727551241,
        sys = Sys(type = 1, id = 2519, country = "US", sunrise = 1727492404, sunset = 1727535204),
        timezone = -14400,
        id = 4140963,
        language = 0,
        name = "Washington",
        cod = 200,
        isFavorite = false,
        lat = 38.9072,
        lon = -77.0369
    ), ExampleJson2KtKotlin(
        coord = Coord(lon = 13.4049, lat = 52.5200),
        weather = arrayListOf(Weather(id = 801, main = "Clouds", description = "few clouds", icon = "02d")),
        base = "stations",
        main = Main(temp = 288.48, feelsLike = 287.75, tempMin = 287.15, tempMax = 289.15, pressure = 1017, humidity = 68, seaLevel = 1017, grndLevel = 1012),
        visibility = 10000,
        wind = Wind(speed = 2.06, deg = 310),
        clouds = Clouds(all = 20),
        dt = 1727551241,
        sys = Sys(type = 1, id = 2518, country = "DE", sunrise = 1727495204, sunset = 1727538266),
        timezone = 7200,
        id = 2950159,
        language = 0,
        name = "Berlin",
        cod = 200,
        isFavorite = false,
        lat = 52.5200,
        lon = 13.4049
    ), ExampleJson2KtKotlin(
        coord = Coord(lon = 13.4049, lat = 52.5200),
        weather = arrayListOf(Weather(id = 801, main = "Clouds", description = "few clouds", icon = "02d")),
        base = "stations",
        main = Main(temp = 288.48, feelsLike = 287.75, tempMin = 287.15, tempMax = 289.15, pressure = 1017, humidity = 68, seaLevel = 1017, grndLevel = 1012),
        visibility = 10000,
        wind = Wind(speed = 2.06, deg = 310),
        clouds = Clouds(all = 20),
        dt = 1727551241,
        sys = Sys(type = 1, id = 2518, country = "DE", sunrise = 1727495204, sunset = 1727538266),
        timezone = 7200,
        id = 2950159,
        language = consts.en.ordinal,
        name = "Berlin",
        cod = 200,
        isFavorite = false,
        lat = 52.5200,
        lon = 13.4049
    )
)
val alerts = arrayOf(
    MyAlerts(name = "Alert 1", id = 1, type = 1, start = 1633036800000, end = 1633040400000, lat = 37.7749, lon = -122.4194),
    MyAlerts(name = "Alert 2", id = 2, type = 2, start = 1633044000000, end = 1633047600000, lat = 40.7128, lon = -74.0060),
    MyAlerts(name = "Alert 3", id = 3, type = 3, start = 1633051200000, end = 1633054800000, lat = 34.0522, lon = -118.2437),
    MyAlerts(name = "Alert 4", id = 4, type = 1, start = 1633058400000, end = 1633062000000, lat = 48.8566, lon = 2.3522)
)
val favoriteLocations = arrayOf(
    Favorites(name = "New York", lat = 40.7128, lon = -74.0060),
    Favorites(name = "Los Angeles", lat = 34.0522, lon = -118.2437),
    Favorites(name = "Tokyo", lat = 35.6762, lon = 139.6503),
    Favorites(name = "Paris", lat = 48.8566, lon = 2.3522),
    Favorites(name = "Sydney", lat = -33.8688, lon = 151.2093)
)