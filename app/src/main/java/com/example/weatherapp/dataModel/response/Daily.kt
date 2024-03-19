package com.example.weatherapp.dataModel.response

data class Daily(
    val apparent_temperature_max: MutableList<String>,
    val apparent_temperature_min: MutableList<String>,
    val sunrise: MutableList<String>,
    val sunset: MutableList<String>,
    val temperature_2m_max: MutableList<String>,
    val temperature_2m_min: MutableList<String>,
    val time: MutableList<String>,
    val weather_code: MutableList<String>
)