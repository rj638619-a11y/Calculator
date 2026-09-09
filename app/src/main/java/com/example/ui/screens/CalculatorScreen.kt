package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
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
import com.example.ui.theme.ThemeMode
import com.example.ui.viewmodel.CalculatorUiState
import com.example.ui.viewmodel.CalculatorViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CalculatorScreen(
    state: CalculatorUiState,
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val theme = state.theme
    val expScrollState = rememberScrollState()
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    var showModeDropdown by remember { mutableStateOf(false) }
    var showCopiedToast by remember { mutableStateOf(false) }
    var dragAccumulator by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
            .padding(top = 8.dp, bottom = 4.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // ----------------------------------------------------
        // Top Section: Mode selector ("Standard ▾"), History icon, Settings icon
        // ----------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mode selector: "Standard ▾"
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (theme.isLight) Color.White else Color(0x33FFFFFF))
                        .border(
                            1.dp,
                            if (theme.isLight) Color(0x0F000000) else Color(0x22FFFFFF),
                            RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            showModeDropdown = true
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("mode_selector_btn"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (state.isScientificExpanded) "Scientific" else "Standard",
                        color = theme.textPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = theme.textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                DropdownMenu(
                    expanded = showModeDropdown,
                    onDismissRequest = { showModeDropdown = false },
                    modifier = Modifier.background(if (theme.isLight) Color.White else Color(0xFF242428))
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Standard",
                                fontWeight = if (!state.isScientificExpanded) FontWeight.Bold else FontWeight.Normal,
                                color = if (!state.isScientificExpanded) theme.primaryAccent else theme.textPrimary
                            )
                        },
                        onClick = {
                            if (state.isScientificExpanded) viewModel.toggleScientific()
                            showModeDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Scientific",
                                fontWeight = if (state.isScientificExpanded) FontWeight.Bold else FontWeight.Normal,
                                color = if (state.isScientificExpanded) theme.primaryAccent else theme.textPrimary
                            )
                        },
                        onClick = {
                            if (!state.isScientificExpanded) viewModel.toggleScientific()
                            showModeDropdown = false
                        }
                    )
                }
            }

            // History icon & Settings icon
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // DEG / RAD Toggle when scientific is active
                if (state.isScientificExpanded) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (theme.isLight) Color.White else Color(0x33FFFFFF))
                            .border(1.dp, if (theme.isLight) Color(0x0F000000) else Color(0x22FFFFFF), CircleShape)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                viewModel.toggleAngleMode()
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (state.angleMode == ExpressionEvaluator.AngleMode.DEG) "DEG" else "RAD",
                            color = theme.primaryAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        viewModel.selectTab(AppNavTab.HISTORY)
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (theme.isLight) Color.White else Color(0x33FFFFFF))
                        .border(1.dp, if (theme.isLight) Color(0x0F000000) else Color(0x22FFFFFF), CircleShape)
                        .testTag("calculator_history_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "History",
                        tint = theme.textPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        viewModel.openSettings()
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (theme.isLight) Color.White else Color(0x33FFFFFF))
                        .border(1.dp, if (theme.isLight) Color(0x0F000000) else Color(0x22FFFFFF), CircleShape)
                        .testTag("calculator_settings_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = theme.textPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // ----------------------------------------------------
        // Display Area:
        // - Small calculation expression (e.g. 2,500 × 4 + 320)
        // - Large right-aligned result number (e.g. 10,320)
        // - Backspace swipe gesture + long press to copy
        // ----------------------------------------------------
        LiquidGlassCard(
            theme = theme,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .padding(vertical = 4.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            val textToCopy = state.evaluatedResult.ifEmpty {
                                state.liveResult.ifEmpty { state.expression.ifEmpty { "0" } }
                            }
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.copyToClipboard(textToCopy)
                            showCopiedToast = true
                            scope.launch {
                                delay(1600)
                                showCopiedToast = false
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { dragAccumulator = 0f },
                        onDragEnd = { dragAccumulator = 0f },
                        onHorizontalDrag = { _, dragAmount ->
                            dragAccumulator += dragAmount
                            if (Math.abs(dragAccumulator) > 35f) {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                viewModel.onBackspace()
                                dragAccumulator = 0f
                            }
                        }
                    )
                },
            shape = RoundedCornerShape(26.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header inside display with copy hint
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (state.hasMemory) {
                        Text(
                            text = "M = ${ExpressionEvaluator.formatResult(state.memoryValue)}",
                            color = theme.primaryAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "Swipe to backspace",
                            color = theme.textSecondary.copy(alpha = 0.5f),
                            fontSize = 11.sp
                        )
                    }

                    if (showCopiedToast) {
                        Text(
                            text = "Copied!",
                            color = theme.primaryAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else if (state.expression.isNotEmpty() || state.evaluatedResult.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                val textToCopy = state.evaluatedResult.ifEmpty {
                                    state.liveResult.ifEmpty { state.expression }
                                }
                                viewModel.copyToClipboard(textToCopy)
                                showCopiedToast = true
                                scope.launch {
                                    delay(1600)
                                    showCopiedToast = false
                                }
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = theme.textSecondary.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Small calculation expression: e.g. 2,500 × 4 + 320
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(expScrollState, reverseScrolling = true),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (state.expression.isEmpty()) "0" else state.expression,
                        color = if (state.isError) Color(0xFFFF5252) else theme.textSecondary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        modifier = Modifier.testTag("calc_expression_text")
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Large right-aligned result number: e.g. 10,320
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    val displayResult = when {
                        state.isError -> state.errorMessage.ifEmpty { "Error" }
                        state.evaluatedResult.isNotEmpty() -> state.evaluatedResult
                        state.liveResult.isNotEmpty() -> state.liveResult
                        state.expression.isNotEmpty() -> state.expression
                        else -> "0"
                    }

                    val resultFontSize = when {
                        displayResult.length > 14 -> 30.sp
                        displayResult.length > 10 -> 36.sp
                        displayResult.length > 7 -> 42.sp
                        else -> 48.sp
                    }

                    AnimatedContent(
                        targetState = displayResult,
                        transitionSpec = {
                            (slideInVertically(
                                animationSpec = spring(
                                    dampingRatio = 0.7f,
                                    stiffness = Spring.StiffnessMedium
                                )
                            ) { it / 3 } + fadeIn()) togetherWith (slideOutVertically { -it / 3 } + fadeOut())
                        },
                        label = "displayResultTransition"
                    ) { targetNum ->
                        Text(
                            text = targetNum,
                            color = if (state.isError) Color(0xFFFF5252) else theme.textPrimary,
                            fontSize = resultFontSize,
                            fontWeight = FontWeight.Light,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            letterSpacing = (-0.5).sp,
                            modifier = Modifier.testTag("calc_result_text")
                        )
                    }
                }
            }
        }

        // ----------------------------------------------------
        // Expandable Scientific Keypad (when toggled via Mode selector)
        // ----------------------------------------------------
        AnimatedVisibility(
            visible = state.isScientificExpanded,
            enter = expandVertically(
                animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMedium),
                expandFrom = Alignment.Top
            ) + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("sin", "cos", "tan", "log", "ln").forEach { func ->
                        LiquidGlassButton(
                            text = func,
                            onClick = { viewModel.onInput(func) },
                            theme = theme,
                            type = CalcButtonType.FUNCTION,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f).height(38.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("asin" to "asin", "acos" to "acos", "atan" to "atan", "√" to "sqrt", "π" to "π").forEach { (lbl, code) ->
                        LiquidGlassButton(
                            text = lbl,
                            onClick = { viewModel.onInput(code) },
                            theme = theme,
                            type = CalcButtonType.FUNCTION,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f).height(38.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("x^y" to "^", "x²" to "^2", "e" to "e", "MC" to "mc", "MR" to "mr").forEach { (lbl, code) ->
                        LiquidGlassButton(
                            text = lbl,
                            onClick = {
                                when (code) {
                                    "mc" -> viewModel.memoryClear()
                                    "mr" -> viewModel.memoryRecall()
                                    else -> viewModel.onInput(code)
                                }
                            },
                            theme = theme,
                            type = CalcButtonType.FUNCTION,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f).height(38.dp)
                        )
                    }
                }
            }
        }

        // ----------------------------------------------------
        // Keypad:
        // - White rounded buttons
        // - Soft shadows
        // - Orange operator buttons
        // - Large orange "=" button
        // - Smooth tactile spring press animation & haptic click
        // ----------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Row 1: AC, ( ), %, ÷
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val clearText = if (state.expression.isEmpty()) "AC" else "C"
                LiquidGlassButton(
                    text = clearText,
                    onClick = { viewModel.onClear() },
                    theme = theme,
                    type = CalcButtonType.FUNCTION,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_ac"
                )
                LiquidGlassButton(
                    text = "( )",
                    onClick = {
                        val openCount = state.expression.count { it == '(' }
                        val closeCount = state.expression.count { it == ')' }
                        if (openCount > closeCount && state.expression.lastOrNull()?.isDigit() == true) {
                            viewModel.onInput(")")
                        } else {
                            viewModel.onInput("(")
                        }
                    },
                    theme = theme,
                    type = CalcButtonType.FUNCTION,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_parens"
                )
                LiquidGlassButton(
                    text = "%",
                    onClick = { viewModel.onInput("%") },
                    theme = theme,
                    type = CalcButtonType.FUNCTION,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_mod"
                )
                LiquidGlassButton(
                    text = "÷",
                    onClick = { viewModel.onInput("÷") },
                    theme = theme,
                    type = CalcButtonType.OPERATOR,
                    fontSize = 26.sp,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_divide"
                )
            }

            // Row 2: 7, 8, 9, ×
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LiquidGlassButton(
                    text = "7",
                    onClick = { viewModel.onInput("7") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_7"
                )
                LiquidGlassButton(
                    text = "8",
                    onClick = { viewModel.onInput("8") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_8"
                )
                LiquidGlassButton(
                    text = "9",
                    onClick = { viewModel.onInput("9") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_9"
                )
                LiquidGlassButton(
                    text = "×",
                    onClick = { viewModel.onInput("×") },
                    theme = theme,
                    type = CalcButtonType.OPERATOR,
                    fontSize = 26.sp,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_multiply"
                )
            }

            // Row 3: 4, 5, 6, −
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LiquidGlassButton(
                    text = "4",
                    onClick = { viewModel.onInput("4") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_4"
                )
                LiquidGlassButton(
                    text = "5",
                    onClick = { viewModel.onInput("5") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_5"
                )
                LiquidGlassButton(
                    text = "6",
                    onClick = { viewModel.onInput("6") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_6"
                )
                LiquidGlassButton(
                    text = "−",
                    onClick = { viewModel.onInput("-") },
                    theme = theme,
                    type = CalcButtonType.OPERATOR,
                    fontSize = 26.sp,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_minus"
                )
            }

            // Row 4: 1, 2, 3, +
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LiquidGlassButton(
                    text = "1",
                    onClick = { viewModel.onInput("1") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_1"
                )
                LiquidGlassButton(
                    text = "2",
                    onClick = { viewModel.onInput("2") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_2"
                )
                LiquidGlassButton(
                    text = "3",
                    onClick = { viewModel.onInput("3") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_3"
                )
                LiquidGlassButton(
                    text = "+",
                    onClick = { viewModel.onInput("+") },
                    theme = theme,
                    type = CalcButtonType.OPERATOR,
                    fontSize = 26.sp,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_plus"
                )
            }

            // Row 5: +/-, 0, ., = (Large glowing warm orange = button)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LiquidGlassButton(
                    text = "+/-",
                    onClick = { viewModel.onInput("+/-") },
                    theme = theme,
                    type = CalcButtonType.FUNCTION,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_negate"
                )
                LiquidGlassButton(
                    text = "0",
                    onClick = { viewModel.onInput("0") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_0"
                )
                LiquidGlassButton(
                    text = ".",
                    onClick = { viewModel.onInput(".") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_dot"
                )
                LiquidGlassButton(
                    text = "=",
                    onClick = { viewModel.onEquals() },
                    theme = theme,
                    type = CalcButtonType.EQUALS,
                    fontSize = 30.sp,
                    modifier = Modifier.weight(1f).height(62.dp),
                    testTag = "btn_equals"
                )
            }
        }
    }
}
