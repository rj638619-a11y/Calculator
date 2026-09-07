package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ThemeMode

@Composable
fun LiquidGlassCard(
    theme: ThemeMode,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(32.dp),
    borderWidth: Dp = 1.dp,
    highlightIntensity: Float = 0.4f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                // Hardware layer optimization for smooth 60fps compositing
                clip = true
                this.shape = shape
            }
            .clip(shape)
            .background(theme.surfaceGlass)
            .background(Color.White.copy(alpha = 0.12f)) // Soft semi-transparent white glass overlay
            .border(
                width = borderWidth,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.25f * highlightIntensity + 0.15f),
                        theme.borderGlass.copy(alpha = 0.20f * highlightIntensity),
                        Color.White.copy(alpha = 0.12f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(400f, 400f)
                ),
                shape = shape
            )
            .drawBehind {
                // Specular top edge light line
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = Color.White.copy(alpha = 0.35f * highlightIntensity + 0.1f),
                    start = Offset(24.dp.toPx(), strokeWidth),
                    end = Offset(size.width - 24.dp.toPx(), strokeWidth),
                    strokeWidth = strokeWidth
                )
            },
        content = content
    )
}

