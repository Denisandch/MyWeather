package com.example.myweather.network.model.maininfo

import com.example.myweather.network.model.maininfo.forecastday.day.OneDay
import com.squareup.moshi.Json

data class Forecast(
    @Json(name = "forecastday" ) val forecastDay: List<OneDay>
)
