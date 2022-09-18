package com.example.myweather.network.models.maininfo

import com.example.myweather.network.models.maininfo.condition.CurrentCondition
import com.squareup.moshi.Json

data class Current(
    @Json(name = "temp_c") val CurrentTemp: Float,
    @Json(name = "condition") val CurrentCondition: CurrentCondition
)
