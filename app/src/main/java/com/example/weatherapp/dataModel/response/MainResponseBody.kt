package com.example.weatherapp.dataModel.response

data class MainResponseBody(
    val current: Current,
    val current_units: CurrentUnits,
    val daily: Daily,
    val daily_units: DailyUnits,
    val elevation: String,
    val generationtime_ms: Double,
    val hourly: Hourly,
    val hourly_units: HourlyUnits,
    val latitude: Double,
    val longitude: String,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: String
)