package com.example.weatherapp.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherapp.Const
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "cityAccess")

class DataStoreRepository(private val context:Context) {

    suspend fun setCityAccess(number:Int){
        context.dataStore.edit { preferences->
            preferences[Const().cityKeyDS] = number
        }
    }

    suspend fun getCityAccess():Int{
        var index = 0
        val preferences = context.dataStore.data.first()/*map { cityAccess ->
            index = cityAccess[Const().cityKeyDS] ?: 0
        }*/
        return preferences[Const().cityKeyDS]?:0//index
    }

}