package com.example.foodwaste.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CorporateBlueLight = lightColorScheme(
    primary = Color(0xFF1E88E5),
    onPrimary = Color.White,
    secondary = Color(0xFF90CAF9),
    onSecondary = Color.Black,
    background = Color(0xFFF4F7FC),
    surface = Color.White,
    onSurface = Color(0xFF0D47A1),
    error = Color(0xFFE53935)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = CorporateBlueLight,
        typography = Typography,
        content = content
    )
}
