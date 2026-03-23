package com.example.flashgncalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.flashgncalculator.ui.screen.FlashCalculatorScreen
import com.example.flashgncalculator.ui.theme.FlashGnCalculatorTheme

class MainActivity : ComponentActivity() {

    private val viewModel: FlashCalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FlashGnCalculatorTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                FlashCalculatorScreen(
                    uiState = uiState,
                    onGnChanged = viewModel::updateGn,
                    onApertureSelected = viewModel::updateAperture,
                    onDistanceSelected = viewModel::updateDistance,
                    onIsoSelected = viewModel::updateIso
                )
            }
        }
    }
}
