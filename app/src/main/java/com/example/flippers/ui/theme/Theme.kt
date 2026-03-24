package com.example.flippers.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimaryDark,
    onPrimary = BlueOnPrimaryDark,
    primaryContainer = BluePrimaryContainerDark,
    onPrimaryContainer = BlueOnPrimaryContainerDark,
    secondary = BlueSecondaryDark,
    onSecondary = BlueOnSecondaryDark,
    secondaryContainer = BlueSecondaryContainerDark,
    tertiary = BlueTertiaryDark,
    background = BlueBackgroundDark,
    surface = BlueSurfaceDark,
    surfaceVariant = BlueSurfaceVariantDark,
    onBackground = BlueOnBackgroundDark,
    onSurface = BlueOnSurfaceDark,
    outline = BlueOutlineDark,
    error = BlueErrorDark
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimaryLight,
    onPrimary = BlueOnPrimary,
    primaryContainer = BluePrimaryContainerLight,
    onPrimaryContainer = BlueOnPrimaryContainerLight,
    secondary = BlueSecondaryLight,
    onSecondary = BlueOnSecondaryLight,
    secondaryContainer = BlueSecondaryContainerLight,
    tertiary = BlueTertiaryLight,
    background = BlueBackgroundLight,
    surface = BlueSurfaceLight,
    surfaceVariant = BlueSurfaceVariantLight,
    onBackground = BlueOnBackgroundLight,
    onSurface = BlueOnSurfaceLight,
    outline = BlueOutlineLight,
    error = BlueErrorLight
)

@Composable
fun FlippersTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
