package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.CurrencyData
import com.example.data.remote.CurrencyInfo
import com.example.ui.components.CalcButtonType
import com.example.ui.components.LiquidGlassButton
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.ThemeMode
import com.example.ui.viewmodel.CalculatorUiState
import com.example.ui.viewmodel.CalculatorViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen(
    state: CalculatorUiState,
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val theme = state.theme
    val fromInfo = CurrencyData.getCurrency(state.fromCurrency)
    val toInfo = CurrencyData.getCurrency(state.toCurrency)

    var currencyPickerTarget by remember { mutableStateOf<String?>(null) } // "FROM" or "TO"
    var currencySearchQuery by remember { mutableStateOf("") }
    var swapRotation by remember { mutableStateOf(0f) }

    val rotationAngle by animateFloatAsState(
        targetValue = swapRotation,
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "swapRotation"
    )

    val formatCurrency = remember {
        val symbols = DecimalFormatSymbols(Locale.US)
        DecimalFormat("#,##0.00", symbols)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Currency Converter",
                    color = theme.textPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = state.currencyLastUpdated,
                    color = theme.textSecondary.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            }

            IconButton(
                onClick = { viewModel.refreshCurrencyRates() },
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(theme.surfaceGlassLight)
                    .testTag("refresh_currency_btn")
            ) {
                if (state.isCurrencyLoading) {
                    CircularProgressIndicator(
                        color = theme.primaryAccent,
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh rates",
                        tint = theme.primaryAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // ----------------------------------------------------
        // Currency Cards Section (From / To + Quick Swap)
        // ----------------------------------------------------
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // "FROM" Currency Card
            LiquidGlassCard(
                theme = theme,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("currency_from_card"),
                highlightIntensity = 0.5f
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Currency Picker Trigger
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(theme.surfaceGlassLight.copy(alpha = 0.6f))
                            .clickable { currencyPickerTarget = "FROM" }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(text = fromInfo.flag, fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = fromInfo.code,
                            color = theme.textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = theme.textSecondary
                        )
                    }

                    // Input Amount
                    Text(
                        text = if (state.currencyAmount.isEmpty()) "0" else state.currencyAmount,
                        color = theme.primaryAccent,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Swap Button (Positioned gracefully between cards)
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(theme.primaryAccent)
                    .clickable {
                        swapRotation += 180f
                        viewModel.swapCurrencies()
                    }
                    .testTag("swap_currency_btn"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Swap currencies",
                    tint = Color(0xFF030712),
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotationAngle)
                )
            }

            // "TO" Currency Card
            LiquidGlassCard(
                theme = theme,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("currency_to_card"),
                highlightIntensity = 0.5f
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Currency Picker Trigger
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(theme.surfaceGlassLight.copy(alpha = 0.6f))
                            .clickable { currencyPickerTarget = "TO" }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(text = toInfo.flag, fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = toInfo.code,
                            color = theme.textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = theme.textSecondary
                        )
                    }

                    // Converted Output Amount & Copy
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = formatCurrency.format(state.convertedCurrencyAmount),
                            color = theme.textPrimary,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = {
                                viewModel.copyToClipboard(
                                    "${state.currencyAmount} ${state.fromCurrency} = ${formatCurrency.format(state.convertedCurrencyAmount)} ${state.toCurrency}"
                                )
                            },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy converted rate",
                                tint = theme.textSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Live Rate Exchange Summary
        val fromRate = state.currencyRates[state.fromCurrency] ?: 1.0
        val toRate = state.currencyRates[state.toCurrency] ?: 1.0
        val unitRate = if (fromRate > 0) toRate / fromRate else 0.0
        val inverseUnitRate = if (unitRate > 0) 1.0 / unitRate else 0.0

        LiquidGlassCard(
            theme = theme,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            highlightIntensity = 0.25f
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "1 ${state.fromCurrency} = ${String.format(Locale.US, "%.4f", unitRate)} ${state.toCurrency}",
                    color = theme.textSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "1 ${state.toCurrency} = ${String.format(Locale.US, "%.4f", inverseUnitRate)} ${state.fromCurrency}",
                    color = theme.textSecondary.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }

        // ----------------------------------------------------
        // Tactile Glass Numeric Keypad for Currency Input
        // ----------------------------------------------------
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val onKeyClick: (String) -> Unit = { key ->
                when (key) {
                    "C" -> viewModel.setCurrencyAmount("")
                    "DEL" -> {
                        val curr = state.currencyAmount
                        if (curr.isNotEmpty()) viewModel.setCurrencyAmount(curr.dropLast(1))
                    }
                    "." -> {
                        if (!state.currencyAmount.contains(".")) {
                            viewModel.setCurrencyAmount(if (state.currencyAmount.isEmpty()) "0." else "${state.currencyAmount}.")
                        }
                    }
                    else -> {
                        if (state.currencyAmount.length < 12) {
                            if (state.currencyAmount == "0") {
                                viewModel.setCurrencyAmount(key)
                            } else {
                                viewModel.setCurrencyAmount("${state.currencyAmount}$key")
                            }
                        }
                    }
                }
            }

            // Keypad Row 1: 7, 8, 9, C
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                LiquidGlassButton("7", { onKeyClick("7") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.NUMBER)
                LiquidGlassButton("8", { onKeyClick("8") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.NUMBER)
                LiquidGlassButton("9", { onKeyClick("9") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.NUMBER)
                LiquidGlassButton("C", { onKeyClick("C") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.ACTION)
            }

            // Keypad Row 2: 4, 5, 6, Del
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                LiquidGlassButton("4", { onKeyClick("4") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.NUMBER)
                LiquidGlassButton("5", { onKeyClick("5") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.NUMBER)
                LiquidGlassButton("6", { onKeyClick("6") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.NUMBER)
                LiquidGlassButton("Del", { onKeyClick("DEL") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.ACTION, icon = Icons.AutoMirrored.Filled.Backspace)
            }

            // Keypad Row 3: 1, 2, 3, +100
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                LiquidGlassButton("1", { onKeyClick("1") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.NUMBER)
                LiquidGlassButton("2", { onKeyClick("2") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.NUMBER)
                LiquidGlassButton("3", { onKeyClick("3") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.NUMBER)
                LiquidGlassButton("+100", {
                    val curr = state.currencyAmount.toDoubleOrNull() ?: 0.0
                    viewModel.setCurrencyAmount("${(curr + 100).toLong()}")
                }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.FUNCTION, fontSize = 14.sp)
            }

            // Keypad Row 4: 0, ., +10, +1000
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                LiquidGlassButton("0", { onKeyClick("0") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.NUMBER)
                LiquidGlassButton(".", { onKeyClick(".") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.NUMBER)
                LiquidGlassButton("+10", {
                    val curr = state.currencyAmount.toDoubleOrNull() ?: 0.0
                    viewModel.setCurrencyAmount("${(curr + 10).toLong()}")
                }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.FUNCTION, fontSize = 14.sp)
                LiquidGlassButton("+1k", {
                    val curr = state.currencyAmount.toDoubleOrNull() ?: 0.0
                    viewModel.setCurrencyAmount("${(curr + 1000).toLong()}")
                }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.FUNCTION, fontSize = 14.sp)
            }
        }
    }

    // ----------------------------------------------------
    // Currency Selection Modal BottomSheet
    // ----------------------------------------------------
    if (currencyPickerTarget != null) {
        val target = currencyPickerTarget!!
        val sheetState = rememberModalBottomSheetState()

        ModalBottomSheet(
            onDismissRequest = { currencyPickerTarget = null },
            sheetState = sheetState,
            containerColor = Color(0xFF0F172A),
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select ${if (target == "FROM") "Source" else "Target"} Currency",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Search Box
                OutlinedTextField(
                    value = currencySearchQuery,
                    onValueChange = { currencySearchQuery = it },
                    placeholder = { Text("Search by code or country...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = theme.primaryAccent,
                        unfocusedBorderColor = Color.DarkGray
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )

                val filteredCurrencies = remember(currencySearchQuery) {
                    CurrencyData.CURRENCIES.filter {
                        it.code.contains(currencySearchQuery, ignoreCase = true) ||
                                it.name.contains(currencySearchQuery, ignoreCase = true)
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                ) {
                    items(filteredCurrencies) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    if (target == "FROM") {
                                        viewModel.setFromCurrency(item.code)
                                    } else {
                                        viewModel.setToCurrency(item.code)
                                    }
                                    currencyPickerTarget = null
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = item.flag, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = item.code,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = item.name,
                                        color = Color.LightGray,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            Text(
                                text = item.symbol,
                                color = theme.primaryAccent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
