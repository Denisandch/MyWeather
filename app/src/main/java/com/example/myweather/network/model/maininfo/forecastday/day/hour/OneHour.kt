package com.example.myweather.network.model.maininfo.forecastday.day.hour

import com.example.myweather.network.model.maininfo.forecastday.day.hour.condition.HourCondition
import com.squareup.moshi.Json

data class OneHour(
    val time: String,
    @Json(name = "temp_c")val temp: Float,
    val condition: HourCondition
)
