package com.example.flashgncalculator

import androidx.lifecycle.ViewModel
import com.example.flashgncalculator.domain.FlashOption
import com.example.flashgncalculator.domain.calculateFlashOutput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FlashCalculatorViewModel : ViewModel() {

    private val apertureOptions = listOf(
        FlashOption("f/0.95", 0.95),
        FlashOption("f/1.4", 1.4),
        FlashOption("f/1.8", 1.8),
        FlashOption("f/2.0", 2.0),
        FlashOption("f/2.8", 2.8),
        FlashOption("f/3.5", 3.5),
        FlashOption("f/4.0", 4.0),
        FlashOption("f/5.6", 5.6),
        FlashOption("f/8.0", 8.0),
        FlashOption("f/11", 11.0),
        FlashOption("f/16", 16.0),
        FlashOption("f/22", 22.0),
        FlashOption("f/32", 32.0)
    )

    private val distanceOptions = listOf(
        FlashOption("0.5 m", 0.5),
        FlashOption("1.0 m", 1.0),
        FlashOption("1.5 m", 1.5),
        FlashOption("2.0 m", 2.0),
        FlashOption("2.5 m", 2.5),
        FlashOption("3.0 m", 3.0),
        FlashOption("3.5 m", 3.5),
        FlashOption("4.0 m", 4.0),
        FlashOption("4.5 m", 4.5),
        FlashOption("5.0 m", 5.0),
        FlashOption("5.5 m", 5.5),
        FlashOption("6.0 m", 6.0),
        FlashOption("6.5 m", 6.5),
        FlashOption("7.0 m", 7.0),
        FlashOption("7.5 m", 7.5),
        FlashOption("8.0 m", 8.0)
    )

    private val isoOptions = listOf(
        FlashOption("ISO 50", 50.0),
        FlashOption("ISO 100", 100.0),
        FlashOption("ISO 200", 200.0),
        FlashOption("ISO 400", 400.0),
        FlashOption("ISO 800", 800.0),
        FlashOption("ISO 1600", 1600.0),
        FlashOption("ISO 3200", 3200.0),
        FlashOption("ISO 6400", 6400.0)
    )

    private val initialState = FlashCalculatorUiState(
        gn = 20,
        apertureOptions = apertureOptions,
        distanceOptions = distanceOptions,
        isoOptions = isoOptions,
        selectedAperture = apertureOptions.first { it.label == "f/4.0" },
        selectedDistance = distanceOptions.first { it.label == "1.5 m" },
        selectedIso = isoOptions.first { it.label == "ISO 100" },
        output = "",
        isPowerOutOfRange = false,
        powerStatusMessage = null
    )

    private val _uiState = MutableStateFlow(initialState.withCalculatedOutput())
    val uiState: StateFlow<FlashCalculatorUiState> = _uiState.asStateFlow()

    fun updateGn(gn: Int) {
        _uiState.update { it.copy(gn = gn).withCalculatedOutput() }
    }

    fun updateAperture(option: FlashOption) {
        _uiState.update { it.copy(selectedAperture = option).withCalculatedOutput() }
    }

    fun updateDistance(option: FlashOption) {
        _uiState.update { it.copy(selectedDistance = option).withCalculatedOutput() }
    }

    fun updateIso(option: FlashOption) {
        _uiState.update { it.copy(selectedIso = option).withCalculatedOutput() }
    }

    private fun FlashCalculatorUiState.withCalculatedOutput(): FlashCalculatorUiState {
        val result = calculateFlashOutput(
            gn = gn,
            aperture = selectedAperture.value,
            distanceMeters = selectedDistance.value,
            iso = selectedIso.value.toInt()
        )
        return copy(
            output = result.powerLabel,
            isPowerOutOfRange = result.isOutOfRange,
            powerStatusMessage = result.statusMessage
        )
    }
}
