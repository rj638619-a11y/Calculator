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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.window.Dialog
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

    var showThemeDialog by remember { mutableStateOf(false) }
    var showCopiedToast by remember { mutableStateOf(false) }
    var dragAccumulator by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // ----------------------------------------------------
        // Top Minimal Status Bar: Mode, Memory, Quick Light/Dark Toggle
        // ----------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // DEG / RAD Toggle Pill
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(theme.surfaceGlassLight.copy(alpha = if (theme.isLight) 0.7f else 0.5f))
                        .border(
                            width = 0.5.dp,
                            color = theme.borderGlass.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            viewModel.toggleAngleMode()
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("deg_rad_toggle"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (state.angleMode == ExpressionEvaluator.AngleMode.DEG) "DEG" else "RAD",
                        color = theme.primaryAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                // Instant Light / Dark Mode Quick Toggle Pill
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(theme.surfaceGlassLight.copy(alpha = if (theme.isLight) 0.7f else 0.5f))
                        .border(
                            width = 0.5.dp,
                            color = theme.borderGlass.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            viewModel.setTheme(theme.toggleLightDark())
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("quick_light_dark_toggle"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (theme.isLight) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (theme.isLight) "Switch to Dark Mode" else "Switch to Light Mode",
                            tint = theme.primaryAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (theme.isLight) "Light" else "Dark",
                            color = theme.textPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Memory Status Pill (if active)
            if (state.hasMemory) {
                Surface(
                    shape = CircleShape,
                    color = theme.primaryAccent.copy(alpha = 0.2f),
                    modifier = Modifier.clip(CircleShape)
                ) {
                    Text(
                        text = "M = ${ExpressionEvaluator.formatResult(state.memoryValue)}",
                        color = theme.primaryAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            } else {
                Text(
                    text = "Swipe to backspace",
                    color = theme.textSecondary.copy(alpha = 0.55f),
                    fontSize = 11.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            // Copied Floating Indicator
            AnimatedVisibility(
                visible = showCopiedToast,
                enter = fadeIn(tween(150)) + slideInVertically { -it },
                exit = fadeOut(tween(200)) + slideOutVertically { -it }
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(theme.primaryAccent)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Copied!",
                        color = if (theme.isLight) Color.White else Color.Black,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // ----------------------------------------------------
        // Modern Samsung / iOS Style Spacious Expression Display
        // Gestures enabled:
        // - Horizontal Swipe Left/Right -> Backspace
        // - Long Press -> Copy result
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
                            // Swipe threshold to trigger backspace
                            if (Math.abs(dragAccumulator) > 35f) {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                viewModel.onBackspace()
                                dragAccumulator = 0f
                            }
                        }
                    )
                },
            highlightIntensity = if (theme.isLight) 0.3f else 0.5f,
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header Row
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
                        letterSpacing = 1.2.sp
                    )

                    if (state.expression.isNotEmpty() || state.evaluatedResult.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                val textToCopy = state.evaluatedResult.ifEmpty {
                                    state.liveResult.ifEmpty { state.expression }
                                }
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                viewModel.copyToClipboard(textToCopy)
                                showCopiedToast = true
                                scope.launch {
                                    delay(1600)
                                    showCopiedToast = false
                                }
                            },
                            modifier = Modifier.size(28.dp).testTag("copy_calc_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy result",
                                tint = theme.textSecondary.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Active Formula / Expression Line (Horizontal scrollable, iOS/Samsung bold typography)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(expScrollState, reverseScrolling = true),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val expFontSize = when {
                        state.expression.length > 20 -> 24.sp
                        state.expression.length > 14 -> 30.sp
                        state.expression.length > 8 -> 36.sp
                        else -> 42.sp
                    }

                    Text(
                        text = if (state.expression.isEmpty()) "0" else state.expression,
                        color = if (state.isError) Color(0xFFDC2626) else theme.textPrimary,
                        fontSize = expFontSize,
                        fontWeight = FontWeight.Light,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        modifier = Modifier.testTag("calc_expression_text")
                    )
                }

                // Live Preview or Final Evaluated Result / Error
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    val resultText = when {
                        state.isError -> state.errorMessage.ifEmpty { "Error" }
                        state.evaluatedResult.isNotEmpty() -> "= ${state.evaluatedResult}"
                        state.liveResult.isNotEmpty() -> "= ${state.liveResult}"
                        else -> ""
                    }

                    AnimatedContent(
                        targetState = resultText,
                        transitionSpec = {
                            (slideInVertically(
                                animationSpec = spring(
                                    dampingRatio = 0.65f,
                                    stiffness = Spring.StiffnessMedium
                                )
                            ) { height -> height / 2 } + fadeIn(
                                animationSpec = spring(
                                    dampingRatio = 0.65f,
                                    stiffness = Spring.StiffnessMedium
                                )
                            )) togetherWith (slideOutVertically(
                                animationSpec = tween(140, easing = FastOutSlowInEasing)
                            ) { -it / 2 } + fadeOut(
                                animationSpec = tween(140)
                            ))
                        },
                        label = "resultTransition"
                    ) { targetText ->
                        if (targetText.isEmpty()) {
                            Spacer(modifier = Modifier.height(20.dp))
                        } else if (state.isError) {
                            Text(
                                text = targetText,
                                color = Color(0xFFDC2626),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else if (state.evaluatedResult.isNotEmpty()) {
                            Text(
                                text = targetText,
                                color = theme.primaryAccent,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.testTag("calc_evaluated_result")
                            )
                        } else {
                            Text(
                                text = targetText,
                                color = theme.primaryAccent.copy(alpha = if (theme.isLight) 0.9f else 0.8f),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.testTag("calc_live_result")
                            )
                        }
                    }
                }
            }
        }

        // ----------------------------------------------------
        // Samsung One UI Style Quick Action Bar (fx, History, Converter, Theme, Backspace)
        // ----------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Scientific Mode Toggle (fx)
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (state.isScientificExpanded) theme.primaryAccent.copy(alpha = if (theme.isLight) 0.25f else 0.35f)
                        else theme.surfaceGlassLight.copy(alpha = if (theme.isLight) 0.65f else 0.45f)
                    )
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        viewModel.toggleScientific()
                    }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("sci_toggle_btn"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Functions,
                        contentDescription = "Scientific Functions",
                        tint = if (state.isScientificExpanded) theme.primaryAccent else theme.textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "fx",
                        color = if (state.isScientificExpanded) theme.primaryAccent else theme.textSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Quick Unit Converter Switcher
            IconButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    viewModel.selectTab(AppNavTab.UNITS)
                },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(theme.surfaceGlassLight.copy(alpha = if (theme.isLight) 0.65f else 0.4f))
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = "Converter",
                    tint = theme.textSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Quick History
            IconButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    viewModel.selectTab(AppNavTab.HISTORY)
                },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(theme.surfaceGlassLight.copy(alpha = if (theme.isLight) 0.65f else 0.4f))
                    .testTag("quick_history_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "History",
                    tint = theme.textSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Theme Palette Picker
            IconButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    showThemeDialog = true
                },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(theme.surfaceGlassLight.copy(alpha = if (theme.isLight) 0.65f else 0.4f))
                    .testTag("theme_picker_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = "Choose Theme Color",
                    tint = theme.primaryAccent,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Quick Backspace Button
            IconButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    viewModel.onBackspace()
                },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(theme.surfaceGlassLight.copy(alpha = if (theme.isLight) 0.65f else 0.4f))
                    .testTag("toolbar_backspace_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Backspace",
                    tint = theme.primaryAccent,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // ----------------------------------------------------
        // Expandable Scientific Keypad (Samsung / iOS Pro Style)
        // ----------------------------------------------------
        AnimatedVisibility(
            visible = state.isScientificExpanded,
            enter = expandVertically(
                animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
                expandFrom = Alignment.Top
            ) + slideInVertically(
                animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium)
            ) { -it / 3 } + fadeIn(tween(200, easing = FastOutSlowInEasing)),
            exit = shrinkVertically(
                animationSpec = tween(180, easing = FastOutSlowInEasing),
                shrinkTowards = Alignment.Top
            ) + slideOutVertically(
                animationSpec = tween(180, easing = FastOutSlowInEasing)
            ) { -it / 3 } + fadeOut(tween(140))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Sci Row 1: sin, cos, tan, log, ln
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
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
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
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("x^y" to "^", "x²" to "^2", "x³" to "^3", "x!" to "!", "1/x" to "1/").forEach { (label, op) ->
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
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LiquidGlassButton(
                        text = "π",
                        onClick = { viewModel.onInput("π") },
                        theme = theme,
                        type = CalcButtonType.FUNCTION,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f).height(38.dp)
                    )
                    LiquidGlassButton(
                        text = "e",
                        onClick = { viewModel.onInput("e") },
                        theme = theme,
                        type = CalcButtonType.FUNCTION,
                        fontSize = 16.sp,
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
        // Modern Minimalist 4-Column Keypad (iOS & Samsung Style)
        // ----------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Row 1: AC, ( ), %, ÷
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // AC / C button: Changes to "C" if expression is not empty
                val clearText = if (state.expression.isEmpty()) "AC" else "C"
                LiquidGlassButton(
                    text = clearText,
                    onClick = { viewModel.onClear() },
                    theme = theme,
                    type = CalcButtonType.ACTION,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f).height(58.dp),
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
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_parens"
                )
                LiquidGlassButton(
                    text = "%",
                    onClick = { viewModel.onInput("%") },
                    theme = theme,
                    type = CalcButtonType.FUNCTION,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_mod"
                )
                LiquidGlassButton(
                    text = "÷",
                    onClick = { viewModel.onInput("÷") },
                    theme = theme,
                    type = CalcButtonType.OPERATOR,
                    fontSize = 26.sp,
                    modifier = Modifier.weight(1f).height(58.dp),
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
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_7"
                )
                LiquidGlassButton(
                    text = "8",
                    onClick = { viewModel.onInput("8") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_8"
                )
                LiquidGlassButton(
                    text = "9",
                    onClick = { viewModel.onInput("9") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_9"
                )
                LiquidGlassButton(
                    text = "×",
                    onClick = { viewModel.onInput("×") },
                    theme = theme,
                    type = CalcButtonType.OPERATOR,
                    fontSize = 26.sp,
                    modifier = Modifier.weight(1f).height(58.dp),
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
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_4"
                )
                LiquidGlassButton(
                    text = "5",
                    onClick = { viewModel.onInput("5") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_5"
                )
                LiquidGlassButton(
                    text = "6",
                    onClick = { viewModel.onInput("6") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_6"
                )
                LiquidGlassButton(
                    text = "−",
                    onClick = { viewModel.onInput("-") },
                    theme = theme,
                    type = CalcButtonType.OPERATOR,
                    fontSize = 26.sp,
                    modifier = Modifier.weight(1f).height(58.dp),
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
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_1"
                )
                LiquidGlassButton(
                    text = "2",
                    onClick = { viewModel.onInput("2") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_2"
                )
                LiquidGlassButton(
                    text = "3",
                    onClick = { viewModel.onInput("3") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_3"
                )
                LiquidGlassButton(
                    text = "+",
                    onClick = { viewModel.onInput("+") },
                    theme = theme,
                    type = CalcButtonType.OPERATOR,
                    fontSize = 26.sp,
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_plus"
                )
            }

            // Row 5: +/-, 0, ., =
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
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_negate"
                )
                LiquidGlassButton(
                    text = "0",
                    onClick = { viewModel.onInput("0") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_0"
                )
                LiquidGlassButton(
                    text = ".",
                    onClick = { viewModel.onInput(".") },
                    theme = theme,
                    type = CalcButtonType.NUMBER,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_dot"
                )
                LiquidGlassButton(
                    text = "=",
                    onClick = { viewModel.onEquals() },
                    theme = theme,
                    type = CalcButtonType.EQUALS,
                    fontSize = 28.sp,
                    modifier = Modifier.weight(1f).height(58.dp),
                    testTag = "btn_equals"
                )
            }
        }
    }

    // ----------------------------------------------------
    // Modern Theme Selector Dialog (with Light/Dark Filter Tabs & Toggle)
    // ----------------------------------------------------
    if (showThemeDialog) {
        var selectedFilterIndex by remember { mutableIntStateOf(0) } // 0: All, 1: Dark, 2: Light

        Dialog(onDismissRequest = { showThemeDialog = false }) {
            LiquidGlassCard(
                theme = theme,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(32.dp),
                highlightIntensity = if (theme.isLight) 0.3f else 0.5f
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Header with Quick Light/Dark Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Theme Presets",
                                color = theme.textPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Select colors & appearance",
                                color = theme.textSecondary.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }

                        IconButton(
                            onClick = { showThemeDialog = false },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = theme.textSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Filter Mode Tabs: [ All ], [ 🌙 Dark ], [ ☀️ Light ]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(theme.surfaceGlassLight.copy(alpha = if (theme.isLight) 0.7f else 0.4f))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("All (18)", "🌙 Dark", "☀️ Light").forEachIndexed { index, label ->
                            val isTabSelected = selectedFilterIndex == index
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(CircleShape)
                                    .background(
                                        if (isTabSelected) theme.primaryAccent
                                        else Color.Transparent
                                    )
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        selectedFilterIndex = index
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = if (isTabSelected) {
                                        if (theme.isLight) Color.White else Color.Black
                                    } else theme.textSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = if (isTabSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val filteredThemes = ThemeMode.values().filter { mode ->
                        when (selectedFilterIndex) {
                            1 -> !mode.isLight
                            2 -> mode.isLight
                            else -> true
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filteredThemes, key = { it.name }) { mode ->
                            val isSelected = mode == state.theme
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        if (isSelected) theme.primaryAccent.copy(alpha = if (theme.isLight) 0.18f else 0.22f)
                                        else theme.surfaceGlassLight.copy(alpha = if (theme.isLight) 0.6f else 0.35f)
                                    )
                                    .border(
                                        width = if (isSelected) 1.5.dp else 0.5.dp,
                                        color = if (isSelected) mode.primaryAccent else (if (theme.isLight) Color.Black.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.1f)),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        viewModel.setTheme(mode)
                                        showThemeDialog = false
                                    }
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (mode.isLight) Icons.Default.LightMode else Icons.Default.DarkMode,
                                            contentDescription = null,
                                            tint = if (isSelected) mode.primaryAccent else theme.textSecondary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = mode.title,
                                            color = if (isSelected) mode.primaryAccent else theme.textPrimary,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Text(
                                        text = mode.description,
                                        color = theme.textSecondary.copy(alpha = 0.75f),
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }

                                // Color Palette preview circles
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(mode.primaryAccent)
                                            .border(0.5.dp, Color.Black.copy(alpha = 0.15f), CircleShape)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(mode.secondaryAccent)
                                            .border(0.5.dp, Color.Black.copy(alpha = 0.15f), CircleShape)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(mode.backgroundColors.first())
                                            .border(0.5.dp, Color.Black.copy(alpha = 0.15f), CircleShape)
                                    )

                                    if (isSelected) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = mode.primaryAccent,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
