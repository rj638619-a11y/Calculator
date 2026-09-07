package com.example.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

@Composable
fun SmartCalculatorTheme(
    themeMode: ThemeMode = ThemeMode.AURORA,
    content: @Composable () -> Unit
) {
    val animatedPrimary by animateColorAsState(
        targetValue = themeMode.primaryAccent,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "themePrimary"
    )
    val animatedSecondary by animateColorAsState(
        targetValue = themeMode.secondaryAccent,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "themeSecondary"
    )
    val animatedTertiary by animateColorAsState(
        targetValue = themeMode.tertiaryAccent,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "themeTertiary"
    )
    val animatedBackground by animateColorAsState(
        targetValue = themeMode.backgroundColors.first(),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "themeBackground"
    )
    val animatedSurface by animateColorAsState(
        targetValue = themeMode.surfaceGlass,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "themeSurface"
    )
    val animatedOnBackground by animateColorAsState(
        targetValue = themeMode.textPrimary,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "themeOnBackground"
    )
    val animatedOnSurface by animateColorAsState(
        targetValue = themeMode.textPrimary,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "themeOnSurface"
    )

    val colorScheme = if (themeMode.isLight) {
        lightColorScheme(
            primary = animatedPrimary,
            secondary = animatedSecondary,
            tertiary = animatedTertiary,
            background = animatedBackground,
            surface = animatedSurface,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onTertiary = Color.White,
            onBackground = animatedOnBackground,
            onSurface = animatedOnSurface
        )
    } else {
        darkColorScheme(
            primary = animatedPrimary,
            secondary = animatedSecondary,
            tertiary = animatedTertiary,
            background = animatedBackground,
            surface = animatedSurface,
            onPrimary = Color(0xFF030712),
            onSecondary = Color.White,
            onTertiary = Color.White,
            onBackground = animatedOnBackground,
            onSurface = animatedOnSurface
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Alias for preview/test compatibility
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    SmartCalculatorTheme(themeMode = ThemeMode.AURORA, content = content)
}
