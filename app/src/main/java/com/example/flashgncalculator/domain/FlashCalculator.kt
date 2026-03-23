package com.example.flashgncalculator.domain

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class FlashOption(
    val label: String,
    val value: Double
)

data class FlashOutputResult(
    val powerLabel: String,
    val isOutOfRange: Boolean,
    val statusMessage: String?
)

private val powerStops = listOf(
    "1/1" to 1.0,
    "1/2" to 0.5,
    "1/4" to 0.25,
    "1/8" to 0.125,
    "1/16" to 0.0625,
    "1/32" to 0.03125,
    "1/64" to 0.015625,
    "1/128" to 0.0078125
)

fun calculateFlashOutput(
    gn: Int,
    aperture: Double,
    distanceMeters: Double,
    iso: Int
): FlashOutputResult {
    val effectiveGnAtFullPower = gn * sqrt(iso / 100.0)
    val requiredGn = aperture * distanceMeters
    val rawPowerFraction = (requiredGn / effectiveGnAtFullPower).pow(2.0)
    val minPower = powerStops.last().second
    val maxPower = powerStops.first().second
    val isOutOfRange = rawPowerFraction < minPower || rawPowerFraction > maxPower
    val clampedPowerFraction = rawPowerFraction.coerceIn(minPower, maxPower)

    val nearestPowerLabel = powerStops.minBy { (_, value) ->
        abs(value - clampedPowerFraction)
    }.first

    val statusMessage = when {
        rawPowerFraction > maxPower -> "当前参数已超过闪光灯满功率"
        rawPowerFraction < minPower -> "当前参数已低于闪光灯最小功率"
        else -> null
    }

    return FlashOutputResult(
        powerLabel = nearestPowerLabel,
        isOutOfRange = isOutOfRange,
        statusMessage = statusMessage
    )
}
