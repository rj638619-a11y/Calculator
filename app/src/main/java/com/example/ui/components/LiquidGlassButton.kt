package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
    shape: Shape = RoundedCornerShape(20.dp),
    testTag: String = text.lowercase()
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label = "buttonScale"
    )

    val (bgColor, textColor, borderColor) = when (type) {
        CalcButtonType.NUMBER -> Triple(
            theme.buttonNumberColor,
            theme.textPrimary,
            Color(0x33FFFFFF)
        )
        CalcButtonType.OPERATOR -> Triple(
            theme.buttonOpColor,
            theme.primaryAccent,
            theme.primaryAccent.copy(alpha = 0.45f)
        )
        CalcButtonType.FUNCTION -> Triple(
            theme.buttonFuncColor,
            theme.tertiaryAccent,
            theme.tertiaryAccent.copy(alpha = 0.35f)
        )
        CalcButtonType.ACTION -> Triple(
            theme.buttonActionColor,
            Color(0xFFFFD1DC),
            Color(0x66FF5C8A)
        )
        CalcButtonType.EQUALS -> Triple(
            theme.primaryAccent.copy(alpha = 0.85f),
            Color(0xFF030712),
            Color(0x99FFFFFF)
        )
        CalcButtonType.MEMORY -> Triple(
            theme.surfaceGlassLight.copy(alpha = 0.3f),
            theme.textSecondary,
            Color(0x22FFFFFF)
        )
    }

    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .testTag(testTag)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
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
                            bgColor.copy(alpha = if (isPressed) 0.7f else 0.45f),
                            bgColor.copy(alpha = if (isPressed) 0.5f else 0.3f)
                        )
                    )
                }
            )
            .border(
                width = if (type == CalcButtonType.EQUALS) 1.5.dp else 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        borderColor.copy(alpha = if (isPressed) 0.8f else 0.55f),
                        borderColor.copy(alpha = 0.15f),
                        borderColor.copy(alpha = if (isPressed) 0.6f else 0.35f)
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
                    color = Color.White.copy(alpha = if (isPressed) 0.4f else 0.2f),
                    start = Offset(size.width * 0.2f, strokeWidth),
                    end = Offset(size.width * 0.8f, strokeWidth),
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
