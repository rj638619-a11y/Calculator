package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.ExpressionEvaluator
import com.example.ui.components.AppNavTab
import com.example.ui.components.CalcButtonType
import com.example.ui.components.LiquidGlassButton
import com.example.ui.components.LiquidGlassCard
import com.example.ui.viewmodel.CalculatorUiState
import com.example.ui.viewmodel.CalculatorViewModel

@Composable
fun CalculatorScreen(
    state: CalculatorUiState,
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val theme = state.theme
    val expScrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // ----------------------------------------------------
        // Top Glass Control Bar
        // ----------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // DEG / RAD Toggle Button
            LiquidGlassButton(
                text = if (state.angleMode == ExpressionEvaluator.AngleMode.DEG) "DEG" else "RAD",
                onClick = { viewModel.toggleAngleMode() },
                theme = theme,
                type = CalcButtonType.FUNCTION,
                fontSize = 13.sp,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .height(36.dp)
                    .width(68.dp),
                testTag = "deg_rad_toggle"
            )

            // Center: Memory indicator
            if (state.hasMemory) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = theme.primaryAccent.copy(alpha = 0.2f),
                    modifier = Modifier.clip(RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = "M (${ExpressionEvaluator.formatResult(state.memoryValue)})",
                        color = theme.primaryAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(4.dp))
            }

            // Right: Scientific Expand Toggle & Quick History
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Toggle Scientific Keypad
                LiquidGlassButton(
                    text = if (state.isScientificExpanded) "Sci ▲" else "Sci ▼",
                    onClick = { viewModel.toggleScientific() },
                    theme = theme,
                    type = if (state.isScientificExpanded) CalcButtonType.OPERATOR else CalcButtonType.FUNCTION,
                    fontSize = 13.sp,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .width(76.dp),
                    testTag = "sci_toggle_btn"
                )

                Spacer(modifier = Modifier.width(6.dp))

                // History Shortcut
                LiquidGlassButton(
                    text = "History",
                    icon = Icons.Default.History,
                    onClick = { viewModel.selectTab(AppNavTab.HISTORY) },
                    theme = theme,
                    type = CalcButtonType.FUNCTION,
                    shape = CircleShape,
                    modifier = Modifier.size(36.dp),
                    testTag = "quick_history_btn"
                )
            }
        }

        // ----------------------------------------------------
        // Liquid Glass Display Screen
        // ----------------------------------------------------
        LiquidGlassCard(
            theme = theme,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .padding(vertical = 6.dp),
            highlightIntensity = 0.6f
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Copy & Clear Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (state.isScientificExpanded) "SCIENTIFIC" else "STANDARD",
                        color = theme.textSecondary.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.5.sp
                    )

                    Row {
                        if (state.expression.isNotEmpty() || state.evaluatedResult.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    val textToCopy = state.evaluatedResult.ifEmpty { state.liveResult.ifEmpty { state.expression } }
                                    viewModel.copyToClipboard(textToCopy)
                                },
                                modifier = Modifier.size(32.dp).testTag("copy_calc_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy result",
                                    tint = theme.textSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // Active Formula / Expression Line (Scrollable horizontally)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(expScrollState, reverseScrolling = true),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (state.expression.isEmpty()) "0" else state.expression,
                        color = if (state.isError) Color(0xFFFF6B6B) else theme.textPrimary,
                        fontSize = if (state.expression.length > 14) 28.sp else 38.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        modifier = Modifier.testTag("calc_expression_text")
                    )
                }

                // Live Preview or Final Evaluated Result / Error
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    if (state.isError) {
                        Text(
                            text = state.errorMessage.ifEmpty { "Error" },
                            color = Color(0xFFFF5C8A),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else if (state.evaluatedResult.isNotEmpty()) {
                        Text(
                            text = "= ${state.evaluatedResult}",
                            color = theme.primaryAccent,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("calc_evaluated_result")
                        )
                    } else if (state.liveResult.isNotEmpty()) {
                        Text(
                            text = "= ${state.liveResult}",
                            color = theme.primaryAccent.copy(alpha = 0.75f),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.testTag("calc_live_result")
                        )
                    } else {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }

        // ----------------------------------------------------
        // Expandable Scientific Keypad
        // ----------------------------------------------------
        AnimatedVisibility(
            visible = state.isScientificExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // Sci Row 1: sin, cos, tan, log, ln
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    listOf("sin", "cos", "tan", "log", "ln").forEach { func ->
                        LiquidGlassButton(
                            text = func,
                            onClick = { viewModel.onInput(func) },
                            theme = theme,
                            type = CalcButtonType.FUNCTION,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        )
                    }
                }

                // Sci Row 2: asin, acos, atan, √, ∛
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    listOf("asin" to "asin", "acos" to "acos", "atan" to "atan", "√" to "sqrt", "∛" to "cbrt").forEach { (label, func) ->
                        LiquidGlassButton(
                            text = label,
                            onClick = { viewModel.onInput(func) },
                            theme = theme,
                            type = CalcButtonType.FUNCTION,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        )
                    }
                }

                // Sci Row 3: x^y, x², x³, x!, 1/x
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    listOf("x^y" to "x^y", "x²" to "x²", "x³" to "x³", "x!" to "!", "1/x" to "1/x").forEach { (label, op) ->
                        LiquidGlassButton(
                            text = label,
                            onClick = { viewModel.onInput(op) },
                            theme = theme,
                            type = CalcButtonType.FUNCTION,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        )
                    }
                }

                // Sci Row 4: π, e, MC, MR, M+, M-
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    LiquidGlassButton(
                        text = "π",
                        onClick = { viewModel.onInput("π") },
                        theme = theme,
                        type = CalcButtonType.FUNCTION,
                        fontSize = 15.sp,
                        modifier = Modifier.weight(1f).height(38.dp)
                    )
                    LiquidGlassButton(
                        text = "e",
                        onClick = { viewModel.onInput("e") },
                        theme = theme,
                        type = CalcButtonType.FUNCTION,
                        fontSize = 15.sp,
                        modifier = Modifier.weight(1f).height(38.dp)
                    )
                    LiquidGlassButton(
                        text = "MC",
                        onClick = { viewModel.memoryClear() },
                        theme = theme,
                        type = CalcButtonType.MEMORY,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f).height(38.dp)
                    )
                    LiquidGlassButton(
                        text = "MR",
                        onClick = { viewModel.memoryRecall() },
                        theme = theme,
                        type = CalcButtonType.MEMORY,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f).height(38.dp)
                    )
                    LiquidGlassButton(
                        text = "M+",
                        onClick = { viewModel.memoryAdd() },
                        theme = theme,
                        type = CalcButtonType.MEMORY,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f).height(38.dp)
                    )
                    LiquidGlassButton(
                        text = "M-",
                        onClick = { viewModel.memorySubtract() },
                        theme = theme,
                        type = CalcButtonType.MEMORY,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f).height(38.dp)
                    )
                }
            }
        }

        // ----------------------------------------------------
        // Primary Tactile Glass Keypad
        // ----------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row 1: AC, (, ), ⌫, ÷
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LiquidGlassButton(
                    text = "AC",
                    onClick = { viewModel.onClear() },
                    theme = theme,
                    type = CalcButtonType.ACTION,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_ac"
                )
                LiquidGlassButton(
                    text = "(",
                    onClick = { viewModel.onInput("(") },
                    theme = theme,
                    type = CalcButtonType.FUNCTION,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_open_paren"
                )
                LiquidGlassButton(
                    text = ")",
                    onClick = { viewModel.onInput(")") },
                    theme = theme,
                    type = CalcButtonType.FUNCTION,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_close_paren"
                )
                LiquidGlassButton(
                    text = "Del",
                    icon = Icons.AutoMirrored.Filled.Backspace,
                    onClick = { viewModel.onBackspace() },
                    theme = theme,
                    type = CalcButtonType.ACTION,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_backspace"
                )
                LiquidGlassButton(
                    text = "÷",
                    onClick = { viewModel.onInput("÷") },
                    theme = theme,
                    type = CalcButtonType.OPERATOR,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_divide"
                )
            }

            // Row 2: 7, 8, 9, %, ×
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LiquidGlassButton(
                    text = "7",
                    onClick = { viewModel.onInput("7") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_7"
                )
                LiquidGlassButton(
                    text = "8",
                    onClick = { viewModel.onInput("8") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_8"
                )
                LiquidGlassButton(
                    text = "9",
                    onClick = { viewModel.onInput("9") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_9"
                )
                LiquidGlassButton(
                    text = "%",
                    onClick = { viewModel.onInput("%") },
                    theme = theme,
                    type = CalcButtonType.OPERATOR,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_mod"
                )
                LiquidGlassButton(
                    text = "×",
                    onClick = { viewModel.onInput("×") },
                    theme = theme,
                    type = CalcButtonType.OPERATOR,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_multiply"
                )
            }

            // Row 3: 4, 5, 6, x^y (or √), −
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LiquidGlassButton(
                    text = "4",
                    onClick = { viewModel.onInput("4") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_4"
                )
                LiquidGlassButton(
                    text = "5",
                    onClick = { viewModel.onInput("5") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_5"
                )
                LiquidGlassButton(
                    text = "6",
                    onClick = { viewModel.onInput("6") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_6"
                )
                LiquidGlassButton(
                    text = "^",
                    onClick = { viewModel.onInput("^") },
                    theme = theme,
                    type = CalcButtonType.OPERATOR,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_pow"
                )
                LiquidGlassButton(
                    text = "−",
                    onClick = { viewModel.onInput("-") },
                    theme = theme,
                    type = CalcButtonType.OPERATOR,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_minus"
                )
            }

            // Row 4: 1, 2, 3, +/-, +
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LiquidGlassButton(
                    text = "1",
                    onClick = { viewModel.onInput("1") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_1"
                )
                LiquidGlassButton(
                    text = "2",
                    onClick = { viewModel.onInput("2") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_2"
                )
                LiquidGlassButton(
                    text = "3",
                    onClick = { viewModel.onInput("3") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_3"
                )
                LiquidGlassButton(
                    text = "+/-",
                    onClick = { viewModel.onInput("+/-") },
                    theme = theme,
                    type = CalcButtonType.FUNCTION,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_negate"
                )
                LiquidGlassButton(
                    text = "+",
                    onClick = { viewModel.onInput("+") },
                    theme = theme,
                    type = CalcButtonType.OPERATOR,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_plus"
                )
            }

            // Row 5: 0 (span 2), ., = (span 2)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LiquidGlassButton(
                    text = "0",
                    onClick = { viewModel.onInput("0") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(2f).height(56.dp),
                    testTag = "btn_0"
                )
                LiquidGlassButton(
                    text = ".",
                    onClick = { viewModel.onInput(".") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f).height(56.dp),
                    testTag = "btn_dot"
                )
                LiquidGlassButton(
                    text = "=",
                    onClick = { viewModel.onEquals() },
                    theme = theme,
                    type = CalcButtonType.EQUALS,
                    fontSize = 28.sp,
                    modifier = Modifier.weight(2f).height(56.dp),
                    testTag = "btn_equals"
                )
            }
        }
    }
}
