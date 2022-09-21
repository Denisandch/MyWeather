package com.example.myweather.network.model.maininfo.forecastday.day

import com.example.myweather.network.model.maininfo.forecastday.day.oneday.DayWeather
import com.example.myweather.network.model.maininfo.forecastday.day.hour.OneHour
import com.squareup.moshi.Json

data class OneDay(
    val date: String,
    @Json(name = "day" ) val dayWeather: DayWeather,
    @Json(name = "hour" ) val hours: List<OneHour>
)
