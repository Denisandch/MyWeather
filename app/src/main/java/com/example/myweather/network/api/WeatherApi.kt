package com.example.myweather.network.api

import com.example.myweather.network.model.JsonAnswer
import com.example.myweather.utils.Constans.API_KEY
import com.example.myweather.utils.Constans.DAYS_COUNT
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast.json")
    suspend fun getWeekWeather(@Query("key") key: String = API_KEY,
                                @Query("q") city: String,
                                @Query("days") days: String = DAYS_COUNT,
                                @Query("aqi") aqi: String = "no",
                                @Query("alerts") alerts: String = "no"
    ): JsonAnswer
}