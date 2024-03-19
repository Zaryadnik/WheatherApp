package com.example.weatherapp.viewModels

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Const
import com.example.weatherapp.dataModel.response.Daily
import com.example.weatherapp.dataModel.response.Hourly
import com.example.weatherapp.dataModel.response.MainResponseBody
import com.example.weatherapp.repository.DataStoreRepository
import com.example.weatherapp.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository, private val context: Context, private val dataStoreRepository: DataStoreRepository) :
    ViewModel() {

    private var _getWeather: MutableStateFlow<MainResponseBody?> = MutableStateFlow(null)
    var getWeather: StateFlow<MainResponseBody?> = _getWeather

    private var _getTreeHoursInformation: MutableStateFlow<Hourly?> = MutableStateFlow(null)
    var getTreeHoursInformation: StateFlow<Hourly?> = _getTreeHoursInformation

    private var _getDailyRainState: MutableStateFlow<MutableList<Boolean>?> = MutableStateFlow(null)
    var getDailyRainState: StateFlow<MutableList<Boolean>?> = _getDailyRainState

    private var _getDayHourlySchedule: MutableStateFlow<Hourly?> = MutableStateFlow(null)
    var getDayHourlySchedule: StateFlow<Hourly?> = _getDayHourlySchedule

    private var _getCityName: MutableStateFlow<String?> = MutableStateFlow(null)
    var getCityName: StateFlow<String?> = _getCityName

    fun getWeather() {
        viewModelScope.launch {
            var index = dataStoreRepository.getCityAccess()
            Log.e("logg","Secondary $index")
            _getWeather.value = repository.getWeather(
                Const().cityCode[index].second.Latitude,
                Const().cityCode[index].second.Longitude
            )
        }
    }


    /**
     * Принимает dayData  в формате "2024-03-19T06:15". Последнии 6 символов удаляются.
     */
    fun getDayInformation(dayData: String): Daily? {
        if (_getWeather.value == null) return null
        var dayBuff = dayData
        dayBuff = dayData.dropLast(6)
        var day: Daily = Daily(
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf()
        )
        _getWeather.value!!.daily.time.forEachIndexed { index, time ->
            if (time == dayBuff)
                day.apply {
                    this.apparent_temperature_max.add(_getWeather.value!!.daily.apparent_temperature_max[index])
                    this.apparent_temperature_min.add(_getWeather.value!!.daily.apparent_temperature_min[index])
                    this.sunrise.add(_getWeather.value!!.daily.sunrise[index])
                    this.sunset.add(_getWeather.value!!.daily.sunset[index])
                    this.temperature_2m_max.add(_getWeather.value!!.daily.temperature_2m_max[index])
                    this.temperature_2m_min.add(_getWeather.value!!.daily.temperature_2m_min[index])
                    this.weather_code.add(_getWeather.value!!.daily.weather_code[index])
                    this.time.add(_getWeather.value!!.daily.time[index])
                }
        }
        return if (day.time.isEmpty()) return null else day
    }


    /**
     * Принимает currentTime  в формате "2024-03-19T06:15".
     */
    fun getWeatherUpToTreeHours(currentTime: String) {
        if (_getWeather.value == null) return
        var index = _getWeather.value!!.hourly.time.indexOf(roundTime(currentTime))
        val hourly =
            Hourly(
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf()
            )
        if (index == -1) index = 0
        for (i in index..index + 3) {
            Log.e("localError", i.toString())
            hourly.time.add(_getWeather.value!!.hourly.time[i].drop(11))
            hourly.rain.add(_getWeather.value!!.hourly.rain[i])
            hourly.showers.add(_getWeather.value!!.hourly.showers[i])
            hourly.snow_depth.add(_getWeather.value!!.hourly.snow_depth[i])
            hourly.snowfall.add(_getWeather.value!!.hourly.snowfall[i])
            hourly.temperature_2m.add(_getWeather.value!!.hourly.temperature_2m[i])
            hourly.wind_direction_10m.add(_getWeather.value!!.hourly.wind_direction_10m[i])
            hourly.wind_speed_10m.add(_getWeather.value!!.hourly.wind_speed_10m[i])
        }
        if (hourly.time.isNotEmpty())
            hourly.wind_speed_10m.forEach {
                Log.e("localError", it)
            }
        _getTreeHoursInformation.value = hourly
    }

    fun roundTime(time: String): String {
        val minutes: Int = time.drop(14).toInt()
        val hours: Int = time.drop(11).dropLast(3).toInt()
        return if (minutes <= 30)
            time.dropLast(2) + "00"
        else
            time.dropLast(5) + (hours + 1).toString() + ":00"
    }

    fun getDailyRainState() {
        if (_getWeather.value == null) return
        viewModelScope.launch {
            var list = mutableListOf<Boolean>()
            var counter = 0
            var anotherCounter = 23
            var isRain = false
            while (counter < 168) {
                if (_getWeather.value!!.hourly.rain[counter].toFloat() != 0.0f) {
                    isRain = true
                }
                if (isRain) {
                    Log.e("loca", "dfs")
                }
                if (counter == anotherCounter) {
                    list.add(isRain)
                    isRain = false
                    anotherCounter += 24
                }
                counter++
            }
            _getDailyRainState.value = list
        }
    }

    /**
     * Принимает day  в формате "2024-03-19T06:15".
     */
    fun getDayHourlySchedule(day: String) {
        if (_getWeather.value == null) return
        val hourly =
            Hourly(
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf()
            )
        var indexOfFirst = _getWeather.value!!.hourly.time.indexOf(day + "T00:00")
        Log.e("logi", day + "T00:00")
        if (indexOfFirst == -1) indexOfFirst = 0
        for (i in indexOfFirst..indexOfFirst + 23) {
            Log.e("logi", _getWeather.value!!.hourly.time[i])
            hourly.time.add(_getWeather.value!!.hourly.time[i].drop(11))
            hourly.rain.add(_getWeather.value!!.hourly.rain[i])
            hourly.showers.add(_getWeather.value!!.hourly.showers[i])
            hourly.snow_depth.add(_getWeather.value!!.hourly.snow_depth[i])
            hourly.snowfall.add(_getWeather.value!!.hourly.snowfall[i])
            hourly.temperature_2m.add(_getWeather.value!!.hourly.temperature_2m[i])
            hourly.wind_direction_10m.add(_getWeather.value!!.hourly.wind_direction_10m[i])
            hourly.wind_speed_10m.add(_getWeather.value!!.hourly.wind_speed_10m[i])
        }
        _getDayHourlySchedule.value = hourly
    }


    fun setCurrentCityDS(city: String, context: Context) {
        viewModelScope.launch {
            Const().cityCode.forEachIndexed { index, pair ->
                if (pair.first == city) {
                    dataStoreRepository.setCityAccess(index)
                }
            }
        }
    }

    fun getCityName(){
        viewModelScope.launch {
            _getCityName.value = Const().cityCode[dataStoreRepository.getCityAccess()].first
        }
    }


}