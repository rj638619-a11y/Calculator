package com.example.ui.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
    val infiniteTransition = rememberInfiniteTransition(label = "LiquidGlassBgAmbient")

    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    val driftX by infiniteTransition.animateFloat(
        initialValue = -0.05f,
        targetValue = 0.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 11000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "driftX"
    )

    val driftY by infiniteTransition.animateFloat(
        initialValue = -0.04f,
        targetValue = 0.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 13000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "driftY"
    )

    val bgColors = theme.backgroundColors
    val accentColor = theme.primaryAccent
    val secondaryColor = theme.secondaryAccent
    val isLight = theme.isLight

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val w = size.width
                val h = size.height

                // 1. Subtle warm base gradient
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = bgColors,
                        startY = 0f,
                        endY = h
                    )
                )

                // 2. Primary ambient warm glow blob (Top-Right / Upper quadrant as seen in design reference)
                val primaryAlpha = if (isLight) 0.20f * pulseGlow else 0.25f * pulseGlow
                val primaryCenter = Offset(
                    x = w * (0.82f + driftX),
                    y = h * (0.22f + driftY)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            accentColor.copy(alpha = primaryAlpha.coerceIn(0f, 0.45f)),
                            secondaryColor.copy(alpha = (primaryAlpha * 0.45f).coerceIn(0f, 0.30f)),
                            Color.Transparent
                        ),
                        center = primaryCenter,
                        radius = w * 0.82f
                    )
                )

                // 3. Secondary subtle ambient warmth (Bottom-Right / Center as seen in splash & bottom screens)
                val secondaryAlpha = if (isLight) 0.14f * pulseGlow else 0.18f * pulseGlow
                val secondaryCenter = Offset(
                    x = w * (0.90f - driftX),
                    y = h * (0.85f - driftY)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            secondaryColor.copy(alpha = secondaryAlpha.coerceIn(0f, 0.35f)),
                            accentColor.copy(alpha = (secondaryAlpha * 0.35f).coerceIn(0f, 0.20f)),
                            Color.Transparent
                        ),
                        center = secondaryCenter,
                        radius = w * 0.75f
                    )
                )

                // 4. Delicate Top-Left fill light to balance contrast
                if (isLight) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.5f),
                                Color.Transparent
                            ),
                            center = Offset(w * 0.1f, h * 0.05f),
                            radius = w * 0.5f
                        )
                    )
                }
            },
        content = content
    )
}

fun Modifier.softBlurGlass(
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = Color.White,
    borderColor: Color = Color(0x0D000000),
    borderWidth: Dp = 1.dp,
    elevation: Dp = 6.dp
): Modifier = this
    .then(
        if (elevation > 0.dp) {
            Modifier.shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = Color(0x08000000),
                spotColor = Color(0x12000000)
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
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = Color.White,
    borderColor: Color = Color(0x0D000000),
    borderWidth: Dp = 1.dp,
    elevation: Dp = 6.dp
): Modifier = softBlurGlass(
    shape = shape,
    backgroundColor = backgroundColor,
    borderColor = borderColor,
    borderWidth = borderWidth,
    elevation = elevation
)
