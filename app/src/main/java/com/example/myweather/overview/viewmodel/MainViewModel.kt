package com.example.myweather.overview.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.constans.Days
import com.example.myweather.constans.Errors
import com.example.myweather.network.api.WeatherNetwork
import com.example.myweather.network.models.JsonAnswer
import com.example.myweather.network.models.maininfo.forecastday.day.OneDay
import com.example.myweather.network.models.maininfo.forecastday.day.hour.OneHour
import com.example.myweather.overview.model.ShowedData
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.math.roundToInt

const val DEFAULT_CITY = "Moscow"

class MainViewModel : ViewModel() {

    private var city = DEFAULT_CITY
    private var currentDay = Days.FIRST
    private lateinit var answer: JsonAnswer
    private lateinit var daysList: List<OneDay>

    private var _showedData = MutableLiveData<ShowedData>()
    val showedData: LiveData<ShowedData> = _showedData

    private var _error = MutableLiveData<Int>()
    val error: LiveData<Int> = _error

    private var _hoursList = MutableLiveData<List<OneHour>>()
    val hoursList: LiveData<List<OneHour>> = _hoursList

    init {
        viewModelScope.launch {
            updateData()
        }
    }

    suspend fun updateData() {
        try {
            currentDay = Days.FIRST
            downloadData()
            devideData()
            fillShowedData()
        } catch (e: Exception) {
            city = showedData.value?.location ?: DEFAULT_CITY
            _error.value = Errors.ErrorUdate
        }
    }

    private suspend fun downloadData() {
        answer = WeatherNetwork.retrofitService.getWeekWeather(city = city)
    }

    fun setCity(city: String) {
        this.city = city
        viewModelScope.launch {
            updateData()
        }
    }

    private fun devideData() {
        daysList = answer.forecast.forecastDay
        _hoursList.value = daysList[currentDay.ordinal].hours
    }

    fun changeDate() {
        nextDay()
        try {
            _hoursList.value = daysList[currentDay.ordinal].hours
            fillShowedData()
        } catch (e: Exception) {
            _error.value = Errors.ErrorSearch
        }
    }

    private fun nextDay() {
        currentDay = when (currentDay) {
            Days.FIRST -> Days.SECOND
            Days.SECOND -> Days.THIRD
            Days.THIRD -> Days.FOURTH
            Days.FOURTH -> Days.FIFTH
            Days.FIFTH -> Days.SIXTH
            Days.SIXTH -> Days.SEVENTH
            Days.SEVENTH -> Days.FIRST
        }
    }

    private fun chooseWeatherTilte(): String {
        return if (currentDay == Days.FIRST) {
            answer.currentWeather.CurrentCondition.currentWeatherText
        } else {
            daysList[currentDay.ordinal].dayWeather.dayCondition.dayWeatherText
        }
    }

    private fun chooseWeatherIcon(): String {
        return if (currentDay == Days.FIRST) {
            answer.currentWeather.CurrentCondition.currentWeatherIcon
        } else {
            daysList[currentDay.ordinal].dayWeather.dayCondition.dayWeatherIcon
        }
    }

    private fun fillShowedData() {
        _showedData.value = ShowedData(
            isToday = currentDay == Days.FIRST,
            location = answer.location.city,
            currentTemp = answer.currentWeather.CurrentTemp.roundToInt().toString(),
            minTemp = daysList[currentDay.ordinal].dayWeather.minTemp.roundToInt().toString(),
            maxTemp = daysList[currentDay.ordinal].dayWeather.maxTemp.roundToInt().toString(),
            date = daysList[currentDay.ordinal].date,
            localTime = answer.location.currentLocalTime.substringAfter(' '),
            weatherTitle = chooseWeatherTilte(),
            weatherIcon = chooseWeatherIcon()
        )
    }
}