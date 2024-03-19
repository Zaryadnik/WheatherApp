package com.example.weatherapp.retrofit

import com.example.weatherapp.dataModel.response.MainResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("/v1/forecast?current=temperature_2m,is_day,rain,showers,snowfall,wind_speed_10m&hourly=temperature_2m,rain,showers,snowfall,snow_depth,wind_speed_10m,wind_direction_10m&daily=weather_code,temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset&timezone=Europe%2FMoscow")
    suspend fun getWeather(
        @Query("latitude") Latitude: String,
        @Query("longitude") Longitude: String
    ): Response<MainResponseBody>

}