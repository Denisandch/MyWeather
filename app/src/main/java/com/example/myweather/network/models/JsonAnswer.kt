package com.example.myweather.network.models

import com.example.myweather.network.models.maininfo.Current
import com.example.myweather.network.models.maininfo.Forecast
import com.example.myweather.network.models.maininfo.Location

import com.squareup.moshi.Json

data class JsonAnswer(
    val location: Location,
    @Json(name = "current") val currentWeather: Current,
    val forecast: Forecast
)
