package com.example.weatherapp.dataModel.response

data class Hourly(
    val rain: MutableList<String>,
    val showers: MutableList<String>,
    val snow_depth: MutableList<String>,
    val snowfall: MutableList<String>,
    val temperature_2m: MutableList<String>,
    val time: MutableList<String>,
    val wind_direction_10m: MutableList<String>,
    val wind_speed_10m: MutableList<String>
)