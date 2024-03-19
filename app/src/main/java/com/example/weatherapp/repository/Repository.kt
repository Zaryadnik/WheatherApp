package com.example.weatherapp.repository

import com.example.weatherapp.dataModel.response.MainResponseBody
import com.example.weatherapp.retrofit.RetrofitInstance
import retrofit2.Response
import java.lang.Exception

class Repository {
    suspend fun getWeather(latitude:String,longitude:String):MainResponseBody?{
        val weatherResponse = RetrofitInstance.api.getWeather(latitude,longitude)
        return if(weatherResponse.isSuccessful)  weatherResponse.body()
        else null
    }
}