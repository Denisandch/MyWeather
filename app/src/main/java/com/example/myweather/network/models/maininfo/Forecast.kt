package com.example.myweather.network.models.maininfo

import com.example.myweather.network.models.maininfo.forecastday.day.OneDay
import com.squareup.moshi.Json

data class Forecast(
    @Json(name = "forecastday" ) val forecastDay: List<OneDay>
)
