package com.example.weatherapp.dataModel.response

data class HourlyUnits(
    val rain: String,
    val showers: String,
    val snow_depth: String,
    val snowfall: String,
    val temperature_2m: String,
    val time: String,
    val wind_direction_10m: String,
    val wind_speed_10m: String
)