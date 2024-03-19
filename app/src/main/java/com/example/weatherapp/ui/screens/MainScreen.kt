package com.example.weatherapp.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.contentColorFor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import com.example.weatherapp.Const
import com.example.weatherapp.R
import com.example.weatherapp.dataModel.response.Current
import com.example.weatherapp.dataModel.response.CurrentUnits
import com.example.weatherapp.dataModel.response.Daily
import com.example.weatherapp.dataModel.response.DailyUnits
import com.example.weatherapp.dataModel.response.Hourly
import com.example.weatherapp.dataModel.response.HourlyUnits
import com.example.weatherapp.viewModels.MainViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen() {
    val mainVM = getViewModel<MainViewModel>()
    LaunchedEffect(Unit) {
        mainVM.getWeather()
        mainVM.getCityName()
    }
    val weather = mainVM.getWeather.collectAsState()
    mainVM.getWeatherUpToTreeHours(weather.value?.current?.time ?: "")
    mainVM.getDailyRainState()
    val dailyRainState = mainVM.getDailyRainState.collectAsState()
    val treeHours = mainVM.getTreeHoursInformation.collectAsState()
    val dayHourlySchedule = mainVM.getDayHourlySchedule.collectAsState()
    var dailyFromClick by remember{ mutableStateOf(false) }
    val cityName = mainVM.getCityName.collectAsState()

    Log.e("localError", "First")

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
    ) {

        SearchCity(viewModel = mainVM)

        Spacer(modifier = Modifier.height(32.dp))

        if (weather.value != null && treeHours.value != null && treeHours.value!!.time.isNotEmpty())
            DetailsBox(
                currentWeather = weather.value!!.current,
                currentUnits = weather.value!!.current_units,
                treeHours = treeHours.value!!,
                hourlyUnits = weather.value!!.hourly_units,
                cityName = cityName.value?:""
            )

        Spacer(modifier = Modifier.height(32.dp))

        if (weather.value != null && dailyRainState.value != null)
            DailyRow(
                daily = weather.value!!.daily,
                dailyUnits = weather.value!!.daily_units,
                rainState = dailyRainState.value!!,
                onClick = { dailyFromClick = it },
                viewModel = mainVM
            )

        Spacer(modifier = Modifier.height(32.dp))

        if (dailyFromClick && dayHourlySchedule.value != null && weather.value != null)
            DayHourlySchedule(
                dayHourlySchedule = dayHourlySchedule.value!!,
                weather.value!!.hourly_units
            )
    }
}

@Composable
private fun DetailsBox(
    currentWeather: Current,
    currentUnits: CurrentUnits,
    treeHours: Hourly,
    hourlyUnits: HourlyUnits,
    cityName:String
) {
    Log.e("localError", "Second")
    Column(
        Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp)
            .background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Text(//Name Of City
            text = cityName,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = currentWeather.temperature_2m,
                fontSize = MaterialTheme.typography.displayLarge.fontSize,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = currentUnits.temperature_2m,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        Text(
            text = "Вероятность дождя " + currentWeather.rain,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))


        Text(
            text = "Вероятность снега " + currentWeather.snowfall,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Часовой прогноз:",
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        repeat(4) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.padding(start = 12.dp)
            ) {

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = treeHours.time[it],
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(32.dp))
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = treeHours.temperature_2m[it],
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = hourlyUnits.temperature_2m,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(32.dp))
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = treeHours.rain[it],
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(32.dp))
                Image(
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp),
                    imageVector = if (treeHours.rain[it].toFloat() > 0.1) ImageVector.vectorResource(
                        R.drawable.cloud_rain
                    )
                    else ImageVector.vectorResource(R.drawable.sun),
                    contentDescription = "",
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun DailyRow(
    daily: Daily,
    dailyUnits: DailyUnits,
    rainState: List<Boolean>,
    onClick: (boolean:Boolean) -> Unit,
    viewModel: MainViewModel
) {
    LazyRow {
        itemsIndexed(daily.time) { index, item ->
            var clicked by remember { mutableStateOf(false) }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .height(160.dp)
                    .width(120.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable {
                        if (!clicked) {
                            onClick(true)
                            viewModel.getDayHourlySchedule(item)
                            clicked = true
                        } else {
                            onClick(false)
                            clicked = false
                        }
                    }
            ) {

                Column {
                    Text(
                        text = item,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.padding(start = 12.dp)
                    ) {
                        Text(
                            text = daily.apparent_temperature_max[index],
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = dailyUnits.apparent_temperature_max,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        modifier = Modifier
                            .height(48.dp)
                            .width(48.dp)
                            .align(Alignment.CenterHorizontally),
                        imageVector = if (rainState[index]) ImageVector.vectorResource(R.drawable.cloud_rain)
                        else ImageVector.vectorResource(R.drawable.sun),
                        contentDescription = ""
                    )
                }

            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun DayHourlySchedule(dayHourlySchedule: Hourly, hourlyUnits: HourlyUnits) {
    Log.e("logi", "Enabled")
    Column(
        Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp)
            .background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        repeat(24) {
            Row(
                modifier = Modifier.padding(start = 12.dp)
            ) {

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = dayHourlySchedule.time[it],
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(32.dp))
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = dayHourlySchedule.temperature_2m[it],
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = hourlyUnits.temperature_2m,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(32.dp))
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = dayHourlySchedule.rain[it],
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(32.dp))
                Image(
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp),
                    imageVector = if (dayHourlySchedule.rain[it].toFloat() > 0.1) ImageVector.vectorResource(
                        R.drawable.cloud_rain
                    )
                    else ImageVector.vectorResource(R.drawable.sun),
                    contentDescription = "",
                )
            }
        }
    }

}

@Composable
private fun SearchCity(
    viewModel: MainViewModel
) {
    val context = get<Context>()
    var cityName by remember{ mutableStateOf("") }
    androidx.compose.material.TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .border(1.dp, MaterialTheme.colorScheme.surfaceTint, CircleShape),
        value = cityName,
        onValueChange = {cityName = it},
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.primary,
            disabledTextColor = MaterialTheme.colorScheme.primary,
            backgroundColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        shape = CircleShape,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions {
            viewModel.setCurrentCityDS(cityName, context)
            viewModel.getWeather()
            viewModel.getCityName()
        }
    )
}











































































