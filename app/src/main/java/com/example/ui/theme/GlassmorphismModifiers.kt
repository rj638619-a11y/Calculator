package com.example.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Gradient key colors shifting smoothly: Purple -> Blue -> Teal
private val ShiftingPurple = Color(0xFF7B2CBF)
private val ShiftingIndigo = Color(0xFF4338CA)
private val ShiftingBlue = Color(0xFF0284C7)
private val ShiftingTeal = Color(0xFF0D9488)
private val ShiftingCyan = Color(0xFF00F5D4)

@Composable
fun LiquidGlassBackground(
    theme: ThemeMode,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "LiquidGlassBgShifting")

    // Slow shifting cycle (Purple -> Blue -> Teal -> Purple)
    val colorShiftPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "colorShiftPhase"
    )

    val shiftX by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 16000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shiftX"
    )

    val shiftY by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shiftY"
    )

    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    // Smooth color transitions when theme changes
    val animatedOrb1 by animateColorAsState(
        targetValue = theme.secondaryAccent,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "animatedOrb1"
    )
    val animatedOrb2 by animateColorAsState(
        targetValue = theme.primaryAccent,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "animatedOrb2"
    )
    val animatedOrb3 by animateColorAsState(
        targetValue = theme.tertiaryAccent,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "animatedOrb3"
    )

    val (dynamicOrb1Color, dynamicOrb2Color, dynamicOrb3Color) = when {
        colorShiftPhase < 1f -> {
            val frac = colorShiftPhase
            Triple(
                lerpColor(animatedOrb1, animatedOrb2, frac),
                lerpColor(animatedOrb2, animatedOrb3, frac),
                lerpColor(animatedOrb3, animatedOrb1, frac)
            )
        }
        colorShiftPhase < 2f -> {
            val frac = colorShiftPhase - 1f
            Triple(
                lerpColor(animatedOrb2, animatedOrb3, frac),
                lerpColor(animatedOrb3, animatedOrb1, frac),
                lerpColor(animatedOrb1, animatedOrb2, frac)
            )
        }
        else -> {
            val frac = colorShiftPhase - 2f
            Triple(
                lerpColor(animatedOrb3, animatedOrb1, frac),
                lerpColor(animatedOrb1, animatedOrb2, frac),
                lerpColor(animatedOrb2, animatedOrb3, frac)
            )
        }
    }

    val animatedBg0 by animateColorAsState(
        targetValue = theme.backgroundColors.getOrElse(0) { theme.backgroundColors.firstOrNull() ?: Color.Black },
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "animatedBg0"
    )
    val animatedBg1 by animateColorAsState(
        targetValue = theme.backgroundColors.getOrElse(1) { theme.backgroundColors.firstOrNull() ?: Color.Black },
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "animatedBg1"
    )
    val animatedBg2 by animateColorAsState(
        targetValue = theme.backgroundColors.getOrElse(2) { theme.backgroundColors.firstOrNull() ?: Color.Black },
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "animatedBg2"
    )
    val animatedBg3 by animateColorAsState(
        targetValue = theme.backgroundColors.getOrElse(3) { theme.backgroundColors.firstOrNull() ?: Color.Black },
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "animatedBg3"
    )
    val bgColors = listOf(animatedBg0, animatedBg1, animatedBg2, animatedBg3)

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val w = size.width
                val h = size.height

                // Base deep atmospheric gradient
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = bgColors,
                        startY = 0f,
                        endY = h
                    )
                )

                // Shifting Orb 1: Glow (Top-Left / Central Drift)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            dynamicOrb1Color.copy(alpha = (if (theme.isLight) 0.18f else 0.32f) * pulseGlow),
                            dynamicOrb1Color.copy(alpha = (if (theme.isLight) 0.05f else 0.10f) * pulseGlow),
                            Color.Transparent
                        ),
                        center = Offset(w * (0.2f + 0.6f * shiftX), h * (0.15f + 0.35f * shiftY)),
                        radius = w * 0.85f
                    )
                )

                // Shifting Orb 2: Glow (Bottom-Right / Upward Drift)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            dynamicOrb2Color.copy(alpha = if (theme.isLight) 0.16f else 0.28f),
                            dynamicOrb2Color.copy(alpha = if (theme.isLight) 0.04f else 0.08f),
                            Color.Transparent
                        ),
                        center = Offset(w * (0.85f - 0.55f * shiftY), h * (0.65f + 0.25f * shiftX)),
                        radius = w * 0.90f
                    )
                )

                // Shifting Orb 3: Accent Glow (Bottom-Center Pulse)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            dynamicOrb3Color.copy(alpha = (if (theme.isLight) 0.12f else 0.22f) * pulseGlow),
                            Color.Transparent
                        ),
                        center = Offset(w * (0.5f + 0.3f * shiftX), h * (0.9f - 0.4f * shiftY)),
                        radius = w * 0.7f
                    )
                )
            },
        content = content
    )
}

private fun lerpColor(start: Color, end: Color, fraction: Float): Color {
    val f = fraction.coerceIn(0f, 1f)
    return Color(
        red = start.red + (end.red - start.red) * f,
        green = start.green + (end.green - start.green) * f,
        blue = start.blue + (end.blue - start.blue) * f,
        alpha = start.alpha + (end.alpha - start.alpha) * f
    )
}

/**
 * Glassmorphic surface modifier with soft blur, semi-transparent white overlay,
 * and subtle frosted border for 60fps hardware accelerated rendering.
 */
fun Modifier.softBlurGlass(
    shape: Shape = RoundedCornerShape(32.dp),
    backgroundColor: Color = Color.White.copy(alpha = 0.12f),
    borderColor: Color = Color.White.copy(alpha = 0.15f),
    borderWidth: Dp = 1.dp,
    elevation: Dp = 0.dp
): Modifier = this
    .then(
        if (elevation > 0.dp) {
            Modifier.shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = borderColor.copy(alpha = 0.2f),
                spotColor = borderColor.copy(alpha = 0.35f)
            )
        } else Modifier
    )
    .clip(shape)
    .background(backgroundColor)
    .border(
        border = BorderStroke(borderWidth, borderColor),
        shape = shape
    )

fun Modifier.liquidGlass(
    shape: Shape = RoundedCornerShape(32.dp),
    backgroundColor: Color = Color(0x33101E2E),
    borderColor: Color = Color.White.copy(alpha = 0.15f),
    borderWidth: Dp = 1.dp,
    elevation: Dp = 0.dp
): Modifier = this
    .then(
        if (elevation > 0.dp) {
            Modifier.shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = borderColor.copy(alpha = 0.3f),
                spotColor = borderColor.copy(alpha = 0.5f)
            )
        } else Modifier
    )
    .clip(shape)
    .background(backgroundColor)
    .background(Color.White.copy(alpha = 0.12f))
    .border(
        width = borderWidth,
        brush = Brush.linearGradient(
            colors = listOf(
                borderColor.copy(alpha = 0.55f),
                Color.White.copy(alpha = 0.15f),
                borderColor.copy(alpha = 0.35f)
            ),
            start = Offset(0f, 0f),
            end = Offset(300f, 400f)
        ),
        shape = shape
    )

