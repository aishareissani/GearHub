package org.umn.gearhub.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Blue,
    onPrimary = Normal,
    secondary = Yellow,
    onSecondary = Black,
    background = Light,
    onBackground = Black,
    surface = Normal,
    onSurface = Black,
    surfaceVariant = NormalHover,
    onSurfaceVariant = Darker,
    outline = Dark,
    error = Color(0xFFD32F2F),
    onError = Normal
)

@Composable
fun GearHubTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}