package com.example.weatherapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

class Const {

    val cityCode:List<Pair<String,CityPointAccess>> =
        listOf(
            Pair("Moscow", CityPointAccess(Latitude = "55.7522", Longitude = "37.6156")),
            Pair("Krasnodar", CityPointAccess(Latitude = "45.0448", Longitude = "38.976")),
            Pair("Novosibirsk", CityPointAccess(Latitude = "55.0415", Longitude = "82.9346")),
            Pair("Stavropol", CityPointAccess(Latitude = "45.0428", Longitude = "41.9734"))
        )

    val cityKeyDS = intPreferencesKey("cityAccess")

}

data class CityPointAccess(
    val Latitude:String,
    val Longitude:String
)