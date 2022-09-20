package com.example.myweather.overview.model

data class ShowedData(
    var isToday: Boolean = true,
    var location: String = "Moscow",
    var currentTemp: String,
    var minTemp: String,
    var maxTemp: String,
    var date: String,
    var localTime: String,
    var weatherTitle: String,
    var weatherIcon: String

)
