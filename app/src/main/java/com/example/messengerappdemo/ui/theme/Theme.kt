package com.example.messengerappdemo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun MessengerAppDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
private val DarkColorPalette = darkColors(
    primary = MainBlue,
    primaryVariant = LightBlue,
    secondary = MainPink
)
private val LightColorPalette = lightColors(
    primary = MainBlue,
    primaryVariant =DarkBlue,
    secondary = MainPink
)