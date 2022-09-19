package com.example.myweather.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.constans.Days
import com.example.myweather.databinding.OneHourBinding
import com.example.myweather.network.api.WeatherNetwork
import com.example.myweather.network.models.JsonAnswer
import com.example.myweather.network.models.maininfo.forecastday.day.OneDay
import com.example.myweather.network.models.maininfo.forecastday.day.hour.OneHour
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private var currentDay = Days.FIRST


    private lateinit var answer: JsonAnswer

    private var _city = MutableLiveData<String>()
    val city: LiveData<String> = _city

    private var _hoursList = MutableLiveData<List<OneHour>>()
    val hoursList: LiveData<List<OneHour>> = _hoursList

    private var _daysList = MutableLiveData<List<OneDay>>()
    val daysList: LiveData<List<OneDay>> = _daysList

    init {
        viewModelScope.launch {
            downloadData()
            devideData()
        }
    }
    private suspend fun downloadData() {
        answer = WeatherNetwork.retrofitService.getWeekWeather(city = "London")
    }

    private fun devideData() {
        _daysList.value = answer.forecast.forecastDay
        _hoursList.value = _daysList.value!![currentDay.ordinal].hours
    }

    private fun updateData() {

    }

}