package com.example.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ThemeMode

enum class CalcButtonType {
    NUMBER,
    OPERATOR,
    FUNCTION,
    ACTION,
    EQUALS,
    MEMORY
}

@Composable
fun LiquidGlassButton(
    text: String,
    onClick: () -> Unit,
    theme: ThemeMode,
    modifier: Modifier = Modifier,
    type: CalcButtonType = CalcButtonType.NUMBER,
    icon: ImageVector? = null,
    fontSize: TextUnit = 22.sp,
    shape: Shape = CircleShape,
    testTag: String = text.lowercase()
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Spring-based animation for button press (0.96f scale with bounce-back on release)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1.0f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = Spring.StiffnessMedium
        ),
        label = "buttonScale"
    )

    val (bgColor, textColor, borderColor) = when (type) {
        CalcButtonType.NUMBER -> Triple(
            theme.buttonNumberColor,
            theme.textPrimary,
            if (theme.isLight) Color.Black.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.15f)
        )
        CalcButtonType.OPERATOR -> Triple(
            theme.buttonOpColor,
            if (theme == ThemeMode.IOS_LIGHT) Color.White else theme.primaryAccent,
            theme.primaryAccent.copy(alpha = if (theme.isLight) 0.35f else 0.5f)
        )
        CalcButtonType.FUNCTION -> Triple(
            theme.buttonFuncColor,
            if (theme.isLight) theme.primaryAccent else theme.tertiaryAccent,
            if (theme.isLight) theme.primaryAccent.copy(alpha = 0.25f) else theme.tertiaryAccent.copy(alpha = 0.4f)
        )
        CalcButtonType.ACTION -> Triple(
            theme.buttonActionColor,
            if (theme.isLight) Color(0xFFDC2626) else Color(0xFFFFD1DC),
            if (theme.isLight) Color(0x33DC2626) else Color(0x66FF5C8A)
        )
        CalcButtonType.EQUALS -> Triple(
            theme.primaryAccent.copy(alpha = 0.95f),
            if (theme.isLight) Color.White else Color(0xFF030712),
            if (theme.isLight) theme.primaryAccent else Color.White.copy(alpha = 0.5f)
        )
        CalcButtonType.MEMORY -> Triple(
            theme.surfaceGlassLight.copy(alpha = if (theme.isLight) 0.6f else 0.35f),
            theme.textSecondary,
            if (theme.isLight) Color.Black.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.15f)
        )
    }

    Box(
        modifier = modifier
            // 60fps GPU-accelerated transformation layer
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                clip = true
                this.shape = shape
            }
            .clip(shape)
            .testTag(testTag)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    color = if (type == CalcButtonType.EQUALS) Color(0xFF030712).copy(alpha = 0.3f) else Color.White.copy(alpha = 0.3f)
                ),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                }
            )
            .background(
                if (type == CalcButtonType.EQUALS) {
                    theme.getActionGradient()
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            bgColor.copy(alpha = if (isPressed) 0.75f else 0.50f),
                            bgColor.copy(alpha = if (isPressed) 0.55f else 0.35f)
                        )
                    )
                }
            )
            .background(Color.White.copy(alpha = if (isPressed) 0.16f else 0.10f)) // Semi-transparent glass overlay
            .border(
                width = if (type == CalcButtonType.EQUALS) 1.5.dp else 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        borderColor.copy(alpha = if (isPressed) 0.85f else 0.55f),
                        Color.White.copy(alpha = 0.15f),
                        borderColor.copy(alpha = if (isPressed) 0.65f else 0.35f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(150f, 150f)
                ),
                shape = shape
            )
            .drawBehind {
                // Top specular highlight line
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = Color.White.copy(alpha = if (isPressed) 0.5f else 0.25f),
                    start = Offset(size.width * 0.15f, strokeWidth),
                    end = Offset(size.width * 0.85f, strokeWidth),
                    strokeWidth = strokeWidth
                )
            }
            .padding(horizontal = 4.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = textColor
            )
        } else {
            Text(
                text = text,
                color = textColor,
                fontSize = fontSize,
                fontWeight = if (type == CalcButtonType.NUMBER || type == CalcButtonType.EQUALS) FontWeight.SemiBold else FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )
        }
    }
}

