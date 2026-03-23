package com.example.flashgncalculator

import com.example.flashgncalculator.domain.FlashOption

data class FlashCalculatorUiState(
    val gn: Int,
    val apertureOptions: List<FlashOption>,
    val distanceOptions: List<FlashOption>,
    val isoOptions: List<FlashOption>,
    val selectedAperture: FlashOption,
    val selectedDistance: FlashOption,
    val selectedIso: FlashOption,
    val output: String,
    val isPowerOutOfRange: Boolean,
    val powerStatusMessage: String?
)
