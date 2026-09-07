package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ThemeMode

@Composable
fun LiquidGlassCard(
    theme: ThemeMode,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    borderWidth: Dp = 1.dp,
    highlightIntensity: Float = 0.4f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(theme.surfaceGlass)
            .border(
                width = borderWidth,
                brush = Brush.linearGradient(
                    colors = listOf(
                        theme.borderGlass.copy(alpha = 0.55f * highlightIntensity),
                        Color.White.copy(alpha = 0.08f),
                        theme.borderGlass.copy(alpha = 0.35f * highlightIntensity)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(400f, 400f)
                ),
                shape = shape
            )
            .drawBehind {
                // Subtle top edge light line
                drawLine(
                    color = Color.White.copy(alpha = 0.25f * highlightIntensity),
                    start = Offset(24.dp.toPx(), 1.dp.toPx()),
                    end = Offset(size.width - 24.dp.toPx(), 1.dp.toPx()),
                    strokeWidth = 1.dp.toPx()
                )
            },
        content = content
    )
}
