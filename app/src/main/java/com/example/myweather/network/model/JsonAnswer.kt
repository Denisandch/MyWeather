package com.example.myweather.network.model

import com.example.myweather.network.model.maininfo.Current
import com.example.myweather.network.model.maininfo.Forecast
import com.example.myweather.network.model.maininfo.Location

import com.squareup.moshi.Json

data class JsonAnswer(
    val location: Location,
    @Json(name = "current") val currentWeather: Current,
    val forecast: Forecast
)
