package com.example.panpanweather_niki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.panpanweather_niki.data.container.AppContainer
import com.example.panpanweather_niki.ui.theme.PanPanWeather_NikiTheme
import com.example.panpanweather_niki.ui.view.MainView
import com.example.panpanweather_niki.ui.viewmodel.ViewModel

class MainActivity : ComponentActivity() {

    private val appContainer = AppContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PanPanWeather_NikiTheme {
                val viewModel = ViewModel(appContainer.weatherRepository)
                MainView(
                    modifier = Modifier.padding(),
                    viewModel = viewModel
                )
            }
        }
    }
}
