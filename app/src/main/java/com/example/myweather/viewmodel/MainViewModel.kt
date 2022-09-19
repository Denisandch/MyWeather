package com.example.myweather.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.databinding.OneHourBinding
import com.example.myweather.network.api.WeatherNetwork
import com.example.myweather.network.models.JsonAnswer
import com.example.myweather.network.models.maininfo.forecastday.day.OneDay
import com.example.myweather.network.models.maininfo.forecastday.day.hour.OneHour
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private lateinit var answer: JsonAnswer

    private var _hoursList = MutableLiveData<List<OneHour>>()
    val hoursList: LiveData<List<OneHour>> = _hoursList

    private var _daysList = MutableLiveData<List<OneDay>>()
    val daysList: LiveData<List<OneDay>> = _daysList

    init {
        Log.i("test", "Зашел в инит")
        viewModelScope.launch {
            Log.i("test", "загрузка")
            answer = WeatherNetwork.retrofitService.getWeekWeather(city = "London")
            Log.i("test", "${answer.location.city}1")
            _daysList.value = answer.forecast.forecastDay
            _hoursList.value = _daysList.value!![0].hours
            Log.i("test", "${(answer.forecast.forecastDay).toString()}2")
        }
    }

}