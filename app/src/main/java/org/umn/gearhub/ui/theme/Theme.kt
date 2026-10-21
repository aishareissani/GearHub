package org.umn.gearhub.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Blue,
    onPrimary = Normal,
    primaryContainer = BlueContainer,
    onPrimaryContainer = OnBlueContainer,
    inversePrimary = Yellow,
    secondary = Yellow,
    onSecondary = Black,
    secondaryContainer = YellowContainer,
    onSecondaryContainer = OnYellowContainer,
    tertiary = Green,
    onTertiary = Normal,
    tertiaryContainer = BlueContainer,
    onTertiaryContainer = OnBlueContainer,
    background = Light,
    onBackground = Black,
    surface = Normal,
    onSurface = Black,
    surfaceVariant = NormalHover,
    onSurfaceVariant = Darker,
    surfaceTint = Blue,
    inverseSurface = Black,
    inverseOnSurface = Normal,
    outline = Dark,
    outlineVariant = NormalActive,
    error = ErrorRed,
    onError = Normal,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    surfaceContainerLowest = Normal,
    surfaceContainerLow = Light,
    surfaceContainer = Normal,
    surfaceContainerHigh = Normal,
    surfaceContainerHighest = Light
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