package com.example.panpanweather_niki.data.container

import com.example.panpanweather_niki.data.repository.WeatherRepository
import com.example.panpanweather_niki.data.service.WeatherApiService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

class AppContainer {
    companion object {
        val baseUrl = "https://api.openweathermap.org/"
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .baseUrl(baseUrl)
        .build()

    private val weatherService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }

    val weatherRepository: WeatherRepository by lazy {
        WeatherRepository(weatherService)
    }

}
