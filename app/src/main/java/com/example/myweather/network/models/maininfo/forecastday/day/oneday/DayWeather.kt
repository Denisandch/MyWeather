package com.example.myweather.network.models.maininfo.forecastday.day.oneday

import com.example.myweather.network.models.maininfo.forecastday.day.oneday.condition.DayCondition
import com.squareup.moshi.Json

data class DayWeather (
    @Json(name = "maxtemp_c") val maxTemp: Float,
    @Json(name = "mintemp_c") val minTemp: Float,
    @Json(name = "avgtemp_c") val avgTemp: Float,
    @Json(name = "condition") val dayCondition: DayCondition
        )