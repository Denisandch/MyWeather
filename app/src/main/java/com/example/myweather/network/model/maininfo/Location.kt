package com.example.myweather.network.model.maininfo

import com.squareup.moshi.Json

data class Location(
    @Json(name = "name" ) val city: String,
    @Json(name = "localtime" ) val currentLocalTime: String
)
