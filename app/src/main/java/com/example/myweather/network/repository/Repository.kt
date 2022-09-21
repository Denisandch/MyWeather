package com.example.myweather.network.repository

import com.example.myweather.network.api.RetrofitInstance
import com.example.myweather.network.model.JsonAnswer

class Repository {
    suspend fun getWeekWeather(city: String): JsonAnswer {
        return RetrofitInstance.retrofitService.getWeekWeather(city = city)
    }
}