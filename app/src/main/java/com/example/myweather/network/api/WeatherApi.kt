package com.example.myweather.network.api

import com.example.myweather.network.models.JsonAnswer
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "c3f11a91efbd4bceaf6125403221309"
private const val BASE_URL = "https://api.weatherapi.com/"
private const val DAYS_COUNT = "7"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()


interface WeatherApi {
    @GET("v1/forecast.json")
    suspend fun getWeekWeather(@Query("key") key: String = API_KEY,
                                @Query("q") city: String,
                                @Query("days") days: String = DAYS_COUNT,
                                @Query("aqi") aqi: String = "no",
                                @Query("alerts") alerts: String = "no"
    ): JsonAnswer
}

object WeatherNetwork {
    val retrofitService: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }
}