package com.example.weatherapp.dataModel.response

data class DailyUnits(
    val apparent_temperature_max: String,
    val apparent_temperature_min: String,
    val sunrise: String,
    val sunset: String,
    val temperature_2m_max: String,
    val temperature_2m_min: String,
    val time: String,
    val weather_code: String
)