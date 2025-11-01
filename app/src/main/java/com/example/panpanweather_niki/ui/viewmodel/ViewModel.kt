package com.example.panpanweather_niki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.panpanweather_niki.R
import com.example.panpanweather_niki.data.repository.WeatherRepository
import com.example.panpanweather_niki.ui.model.WeatherModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val weather: WeatherModel) : UiState()
    data class Error(val message: String) : UiState()
}


class ViewModel (private val repository: WeatherRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _weather = MutableStateFlow(WeatherModel())
    val weather: StateFlow<WeatherModel> = _weather.asStateFlow()


    private val _weatherConditionIcon = MutableStateFlow<String?>(null)
    val weatherConditionIcon: StateFlow<String?> = _weatherConditionIcon.asStateFlow()

    fun fetchWeather(city: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val weather = repository.getWeather(city)
                _weather.value = weather
                _uiState.value = UiState.Success(weather)
                _weatherConditionIcon.value = repository.getWeatherIconUrl(weather.icon)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "HTTP 404 Not Found")
            }
        }
    }

    fun formattedUpdatedTime(): String {
        val updated = _weather.value.updatedTime
        val timePattern = SimpleDateFormat("h:mm a", Locale.getDefault())
        return try {
            val epoch = updated.toLong()
            val date = if (epoch > 1_000_000_000_000L) Date(epoch) else Date(epoch * 1000L)
            timePattern.format(date)
        } catch (e: Exception) {
            val tried = listOf(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss"
            )
            var parsed: Date? = null
            for (fmt in tried) {
                try {
                    parsed = SimpleDateFormat(fmt, Locale.getDefault()).parse(updated)
                    if (parsed != null) break
                } catch (_: Exception) {}
            }
            if (parsed != null) timePattern.format(parsed) else updated
        }
    }


    fun formatTime(epochSeconds: Int): String {
        val timePattern = SimpleDateFormat("h:mm a", Locale.getDefault())
        return try {
            val epoch = epochSeconds.toLong()
            val date = if (epoch > 1_000_000_000_000L) Date(epoch) else Date(epoch * 1000L)
            timePattern.format(date)
        } catch (e: Exception) {
            ""
        }
    }



    val weatherDetails = weather.map {
        listOf(
            Triple("HUMIDITY", "${it.humidity}%", R.drawable.icon_humidity),
            Triple("WIND", "${it.windSpeed} km/h", R.drawable.icon_wind),
            Triple("FEELS LIKE", "${it.feelsLike}°C", R.drawable.icon_feels_like),
            Triple("RAIN FALL", "${it.rainFall ?: 0} mm", R.drawable.icon_rain),
            Triple("PRESSURE", "${it.pressure} hPa", R.drawable.devices),
            Triple("CLOUDS", "${it.cloud}%", R.drawable.cloud)
        )
    }

    val sunDetails = weather.map {
        listOf(
            Triple("SUNRISE", it.sunriseTime, R.drawable.sunrise),
            Triple("SUNSET", it.sunsetTime, R.drawable.sunset)
        )
    }
}