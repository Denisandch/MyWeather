package com.example.myweather.network.model.maininfo.forecastday.day.hour.condition

import com.squareup.moshi.Json

data class HourCondition(
    @Json(name = "text" ) val hourWeatherText: String,
    @Json(name = "icon" ) val hourWeatherIcon: String
)
