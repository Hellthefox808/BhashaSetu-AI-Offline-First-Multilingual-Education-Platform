package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = CoffeePrimaryDark,
    onPrimary = CoffeeOnPrimaryDark,
    primaryContainer = CoffeePrimaryContainerDark,
    onPrimaryContainer = CoffeeOnPrimaryContainerDark,
    secondary = CoffeeSecondaryDark,
    onSecondary = CoffeeOnSecondaryDark,
    secondaryContainer = CoffeeSecondaryContainerDark,
    onSecondaryContainer = CoffeeOnSecondaryContainerDark,
    tertiary = CoffeeTertiaryDark,
    onTertiary = CoffeeOnTertiaryDark,
    tertiaryContainer = CoffeeTertiaryContainerDark,
    onTertiaryContainer = CoffeeOnTertiaryContainerDark,
    background = CoffeeBackgroundDark,
    surface = CoffeeSurfaceDark,
    surfaceVariant = CoffeeSurfaceVariantDark,
    onBackground = CoffeeTextPrimaryDark,
    onSurface = CoffeeTextPrimaryDark,
    onSurfaceVariant = CoffeeTextSecondaryDark,
    outline = CoffeeOutlineDark,
    outlineVariant = CoffeeOutlineVariantDark
)

private val LightColorScheme = lightColorScheme(
    primary = CoffeePrimary,
    onPrimary = CoffeeOnPrimary,
    primaryContainer = CoffeePrimaryContainer,
    onPrimaryContainer = CoffeeOnPrimaryContainer,
    secondary = CoffeeSecondary,
    onSecondary = CoffeeOnSecondary,
    secondaryContainer = CoffeeSecondaryContainer,
    onSecondaryContainer = CoffeeOnSecondaryContainer,
    tertiary = CoffeeTertiary,
    onTertiary = CoffeeOnTertiary,
    tertiaryContainer = CoffeeTertiaryContainer,
    onTertiaryContainer = CoffeeOnTertiaryContainer,
    background = CoffeeBackgroundLight,
    surface = CoffeeSurfaceLight,
    surfaceVariant = CoffeeSurfaceVariantLight,
    onBackground = CoffeeTextPrimaryLight,
    onSurface = CoffeeTextPrimaryLight,
    onSurfaceVariant = CoffeeTextSecondaryLight,
    outline = CoffeeOutlineLight,
    outlineVariant = CoffeeOutlineVariantLight
)

@Composable
fun MyApplicationTheme(
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


