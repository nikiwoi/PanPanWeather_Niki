package com.example.panpanweather_niki.data.repository

import com.example.panpanweather_niki.data.service.WeatherApiService
import com.example.panpanweather_niki.ui.model.WeatherModel
import com.example.panpanweather_niki.data.dto.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(private val service: WeatherApiService) {
    suspend fun getWeather(city: String): WeatherModel {
        val weathers = service.getWeather(
            cityName = city,
            units = "metric",
            apiKey = "4d3bb678d948b10f53a39eb579896690"
        ).body()!!
        return WeatherModel(
            cityName = weathers.name,
            dateTime = weathers.dt,
            updatedTime = weathers.dt.toString(),
            icon = weathers.weather[0].icon,
            temperature = weathers.main.temp,
            weatherCondition = weathers.weather[0].main,
            humidity = weathers.main.humidity,
            windSpeed = weathers.wind.speed,
            feelsLike = weathers.main.feels_like,
            rainFall = weathers.rain?.`1h` ?: 0.0,
            pressure = weathers.main.pressure,
            cloud = weathers.clouds.all,
            sunriseTime = weathers.sys.sunrise,
            sunsetTime = weathers.sys.sunset
        )
    }

    fun getWeatherIconUrl(iconId: String): String {
        val url ="https://openweathermap.org/img/wn/$iconId@2x.png"
        return url
    }
}
