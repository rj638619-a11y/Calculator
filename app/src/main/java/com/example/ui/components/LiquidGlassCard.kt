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
import androidx.compose.ui.draw.shadow
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
    elevation: Dp = 6.dp,
    highlightIntensity: Float = 0.4f,
    content: @Composable BoxScope.() -> Unit
) {
    val cardBg = if (theme.isLight) {
        Color.White
    } else {
        theme.surfaceGlass
    }

    val cardBorder = if (theme.isLight) {
        Color(0x0F000000)
    } else {
        theme.borderGlass.copy(alpha = 0.35f)
    }

    val shadowSpot = if (theme.isLight) Color(0x14000000) else Color(0x66000000)
    val shadowAmbient = if (theme.isLight) Color(0x08000000) else Color(0x33000000)

    Box(
        modifier = modifier
            .then(
                if (elevation > 0.dp) {
                    Modifier.shadow(
                        elevation = elevation,
                        shape = shape,
                        spotColor = shadowSpot,
                        ambientColor = shadowAmbient
                    )
                } else Modifier
            )
            .clip(shape)
            .background(cardBg)
            .border(
                border = BorderStroke(borderWidth, cardBorder),
                shape = shape
            ),
        content = content
    )
}
