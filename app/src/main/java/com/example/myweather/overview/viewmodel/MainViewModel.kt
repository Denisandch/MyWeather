package com.example.myweather.overview.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.constans.Days
import com.example.myweather.network.api.WeatherNetwork
import com.example.myweather.network.models.JsonAnswer
import com.example.myweather.network.models.maininfo.forecastday.day.OneDay
import com.example.myweather.network.models.maininfo.forecastday.day.hour.OneHour
import com.example.myweather.overview.model.ShowedData
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private var currentDay = Days.FIRST

    private lateinit var answer: JsonAnswer

    private var _showedData = MutableLiveData<ShowedData>()
    val showedData: LiveData<ShowedData> = _showedData

    private var _hoursList = MutableLiveData<List<OneHour>>()
    val hoursList: LiveData<List<OneHour>> = _hoursList

    private var _daysList = MutableLiveData<List<OneDay>>()
    val daysList: LiveData<List<OneDay>> = _daysList

    init {
        Log.i("inter", "Начало инита")
        viewModelScope.launch {
            Log.i("inter", "начало обновления")
            updateData()
        }
        Log.i("inter", "конец инита")
    }

    suspend fun updateData() {
        currentDay = Days.FIRST
        downloadData()
        devideData()
        fillShowedData()
    }

    private suspend fun downloadData() {
        answer = WeatherNetwork.retrofitService.getWeekWeather(city = "Moscow")
    }

    private fun devideData() {
        _daysList.value = answer.forecast.forecastDay
        _hoursList.value = _daysList.value!![currentDay.ordinal].hours
    }

    fun changeDate() {
        nextDay()
        _hoursList.value = _daysList.value!![currentDay.ordinal].hours
        fillShowedData()
    }

    private fun nextDay() {
        currentDay = when(currentDay) {
            Days.FIRST -> Days.SECOND
            Days.SECOND -> Days.THIRD
            Days.THIRD -> Days.FOURTH
            Days.FOURTH -> Days.FIFTH
            Days.FIFTH -> Days.SIXTH
            Days.SIXTH -> Days.SEVENTH
            Days.SEVENTH -> Days.FIRST
        }
    }

    private fun chooseWeatherTilte(): String{
        return if(currentDay == Days.FIRST) {
            answer.currentWeather.CurrentCondition.currentWeatherText
        } else {
            _daysList.value!![currentDay.ordinal].dayWeather.dayCondition.dayWeatherText
        }
    }

    private fun chooseWeatherIcon(): String{
        return if(currentDay == Days.FIRST) {
            answer.currentWeather.CurrentCondition.currentWeatherIcon
        } else {
            _daysList.value!![currentDay.ordinal].dayWeather.dayCondition.dayWeatherIcon
        }
    }

    private fun fillShowedData() {
        _showedData.value = ShowedData(
            isToday = currentDay == Days.FIRST,
            location = answer.location.city,
            currentTemp = answer.currentWeather.CurrentTemp.toString(),
            minTemp = _daysList.value!![currentDay.ordinal].dayWeather.minTemp.toString(),
            maxTemp = _daysList.value!![currentDay.ordinal].dayWeather.maxTemp.toString(),
            date = _daysList.value!![currentDay.ordinal].date,
            localTime = answer.location.currentLocalTime.substringAfter(' '),
            weatherTitle = chooseWeatherTilte(),
            weatherIcon = chooseWeatherIcon()
        )
    }
}