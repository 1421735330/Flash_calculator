package com.example.flashgncalculator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FlashColorScheme = darkColorScheme(
    primary = AppWhite,
    onPrimary = AppBlack,
    background = AppBlack,
    onBackground = AppWhite,
    surface = AppBlack,
    onSurface = AppWhite
)

@Composable
fun FlashGnCalculatorTheme(
    content: @Composable () -> Unit
) {
    isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = FlashColorScheme,
        typography = FlashTypography,
        content = content
    )
}
