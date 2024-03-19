package com.example.weatherapp.dataModel.response

data class Current(
    val interval: String,
    val is_day: String,
    val rain: String,
    val showers: String,
    val snowfall: String,
    val temperature_2m: String,
    val time: String,
    val wind_speed_10m: String
)