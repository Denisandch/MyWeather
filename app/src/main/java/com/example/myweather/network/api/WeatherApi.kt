package com.example.myweather.network.api

import com.example.myweather.network.model.CurrentWeather
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val API_KEY = "c3f11a91efbd4bceaf6125403221309"
private const val BASE_URL = "https://api.weatherapi.com/v1/forecast.json?key=c3f11a91efbd4bceaf6125403221309&q=London&days=1&aqi=no&alerts=yes"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

//TODO edd params, config the func
interface WeatherApi {
    @GET("")
    suspend fun getTodayWeather(city: String): CurrentWeather
}

class WeatherNetwork {
    val retrofitService: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }
}