package com.example.ui.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LiquidGlassBackground(
    theme: ThemeMode,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "LiquidGlassBg")
    
    val shiftX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shiftX"
    )

    val shiftY by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 22000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shiftY"
    )

    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.65f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    val bgColors = theme.backgroundColors
    val pAccent = theme.primaryAccent
    val sAccent = theme.secondaryAccent
    val tAccent = theme.tertiaryAccent

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

                // Liquid floating orb 1 (Primary Neon Glow)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            pAccent.copy(alpha = 0.28f * pulseGlow),
                            pAccent.copy(alpha = 0.08f * pulseGlow),
                            Color.Transparent
                        ),
                        center = Offset(w * (0.2f + 0.6f * shiftX), h * (0.15f + 0.3f * shiftY)),
                        radius = w * 0.75f
                    )
                )

                // Liquid floating orb 2 (Secondary Glow)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            sAccent.copy(alpha = 0.24f),
                            sAccent.copy(alpha = 0.06f),
                            Color.Transparent
                        ),
                        center = Offset(w * (0.8f - 0.5f * shiftY), h * (0.6f + 0.3f * shiftX)),
                        radius = w * 0.85f
                    )
                )

                // Liquid floating orb 3 (Tertiary Accent Glow)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            tAccent.copy(alpha = 0.18f * pulseGlow),
                            Color.Transparent
                        ),
                        center = Offset(w * (0.5f + 0.3f * shiftX), h * (0.9f - 0.4f * shiftY)),
                        radius = w * 0.6f
                    )
                )
            },
        content = content
    )
}

fun Modifier.liquidGlass(
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = Color(0x33101E2E),
    borderColor: Color = Color(0x44FFFFFF),
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
    .border(
        width = borderWidth,
        brush = Brush.linearGradient(
            colors = listOf(
                borderColor.copy(alpha = 0.55f),
                borderColor.copy(alpha = 0.12f),
                borderColor.copy(alpha = 0.35f)
            ),
            start = Offset(0f, 0f),
            end = Offset(300f, 400f)
        ),
        shape = shape
    )
