package com.example.myweather.network.models.maininfo.condition

import com.squareup.moshi.Json

data class CurrentCondition(
    @Json(name = "text" ) val currentWeatherText: String,
    @Json(name = "icon" ) val currentWeatherIcon: String
)
