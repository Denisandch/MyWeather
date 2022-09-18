package com.example.myweather.network.models.maininfo.forecastday.day.hour

import com.example.myweather.network.models.maininfo.forecastday.day.hour.condition.HourCondition

data class OneHour(
    val time: String,
    val temp_c: Float,
    val condition: HourCondition
)
