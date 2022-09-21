package com.example.myweather.network.model.maininfo.forecastday.day.oneday.condition

import com.squareup.moshi.Json

data class DayCondition(
    @Json(name = "text") val dayWeatherText: String,
    @Json(name = "icon") val dayWeatherIcon: String
)
