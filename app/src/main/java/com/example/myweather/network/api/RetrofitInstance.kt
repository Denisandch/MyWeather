package com.example.myweather.network.api

import com.example.myweather.utils.Constans
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constans.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val retrofitService: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }
}