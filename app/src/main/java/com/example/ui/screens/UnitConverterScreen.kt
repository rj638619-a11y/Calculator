package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.UnitCategory
import com.example.engine.UnitConversionEngine
import com.example.engine.UnitItem
import com.example.ui.components.CalcButtonType
import com.example.ui.components.LiquidGlassButton
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.ThemeMode
import com.example.ui.viewmodel.CalculatorUiState
import com.example.ui.viewmodel.CalculatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterScreen(
    state: CalculatorUiState,
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val theme = state.theme
    var unitPickerTarget by remember { mutableStateOf<String?>(null) } // "FROM" or "TO"
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Text(
            text = "Unit Converter",
            color = theme.textPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // ----------------------------------------------------
        // Category Chips (Horizontal Scrollable)
        // ----------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UnitConversionEngine.CATEGORIES.forEach { category ->
                val isSelected = category == state.unitCategory
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (isSelected) theme.primaryAccent.copy(alpha = 0.25f)
                            else theme.surfaceGlassLight.copy(alpha = 0.4f)
                        )
                        .clickable { viewModel.selectUnitCategory(category) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("unit_cat_${category.name.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category.title,
                        color = if (isSelected) theme.primaryAccent else theme.textSecondary,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // ----------------------------------------------------
        // Conversion Cards (From / To)
        // ----------------------------------------------------
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // "FROM" Unit Card
            LiquidGlassCard(
                theme = theme,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("unit_from_card"),
                highlightIntensity = 0.5f
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Unit selector trigger
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(theme.surfaceGlassLight.copy(alpha = 0.6f))
                            .clickable { unitPickerTarget = "FROM" }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${state.fromUnit.name} (${state.fromUnit.symbol})",
                            color = theme.textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = theme.textSecondary
                        )
                    }

                    // Input value
                    Text(
                        text = if (state.unitInputValue.isEmpty()) "0" else state.unitInputValue,
                        color = theme.primaryAccent,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Swap Button
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(theme.primaryAccent)
                    .clickable { viewModel.swapUnits() }
                    .testTag("swap_units_btn"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Swap units",
                    tint = Color(0xFF030712),
                    modifier = Modifier.size(24.dp)
                )
            }

            // "TO" Unit Card
            LiquidGlassCard(
                theme = theme,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("unit_to_card"),
                highlightIntensity = 0.5f
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Unit selector trigger
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(theme.surfaceGlassLight.copy(alpha = 0.6f))
                            .clickable { unitPickerTarget = "TO" }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${state.toUnit.name} (${state.toUnit.symbol})",
                            color = theme.textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = theme.textSecondary
                        )
                    }

                    // Output value + Copy
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = state.unitOutputValue,
                            color = theme.textPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = {
                                viewModel.copyToClipboard(
                                    "${state.unitInputValue} ${state.fromUnit.symbol} = ${state.unitOutputValue} ${state.toUnit.symbol}"
                                )
                            },
                            modifier = Modifier.size(28.dp)
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
        }

        // ----------------------------------------------------
        // Glass Numeric Keypad for Unit Converter
        // ----------------------------------------------------
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val onKeyClick: (String) -> Unit = { key ->
                when (key) {
                    "C" -> viewModel.setUnitInputValue("")
                    "DEL" -> {
                        val curr = state.unitInputValue
                        if (curr.isNotEmpty()) viewModel.setUnitInputValue(curr.dropLast(1))
                    }
                    "." -> {
                        if (!state.unitInputValue.contains(".")) {
                            viewModel.setUnitInputValue(if (state.unitInputValue.isEmpty()) "0." else "${state.unitInputValue}.")
                        }
                    }
                    "+/-" -> {
                        if (state.unitInputValue.startsWith("-")) {
                            viewModel.setUnitInputValue(state.unitInputValue.removePrefix("-"))
                        } else if (state.unitInputValue.isNotEmpty() && state.unitInputValue != "0") {
                            viewModel.setUnitInputValue("-${state.unitInputValue}")
                        }
                    }
                    else -> {
                        if (state.unitInputValue.length < 12) {
                            if (state.unitInputValue == "0") {
                                viewModel.setUnitInputValue(key)
                            } else {
                                viewModel.setUnitInputValue("${state.unitInputValue}$key")
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

            // Keypad Row 3: 1, 2, 3, +/-
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                LiquidGlassButton("1", { onKeyClick("1") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.NUMBER)
                LiquidGlassButton("2", { onKeyClick("2") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.NUMBER)
                LiquidGlassButton("3", { onKeyClick("3") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.NUMBER)
                LiquidGlassButton("+/-", { onKeyClick("+/-") }, theme, Modifier.weight(1f).height(50.dp), CalcButtonType.FUNCTION)
            }

            // Keypad Row 4: 0 (span 2), .
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                LiquidGlassButton("0", { onKeyClick("0") }, theme, Modifier.weight(2f).height(50.dp), CalcButtonType.NUMBER)
                LiquidGlassButton(".", { onKeyClick(".") }, theme, Modifier.weight(2f).height(50.dp), CalcButtonType.NUMBER)
            }
        }
    }

    // ----------------------------------------------------
    // Unit Selection Modal BottomSheet
    // ----------------------------------------------------
    if (unitPickerTarget != null) {
        val target = unitPickerTarget!!
        val sheetState = rememberModalBottomSheetState()
        val unitsList = UnitConversionEngine.UNITS[state.unitCategory] ?: emptyList()

        ModalBottomSheet(
            onDismissRequest = { unitPickerTarget = null },
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
                    text = "Select ${state.unitCategory.title} Unit",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    items(unitsList, key = { it.symbol + it.name }) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CircleShape)
                                .clickable {
                                    if (target == "FROM") {
                                        viewModel.setFromUnit(item)
                                    } else {
                                        viewModel.setToUnit(item)
                                    }
                                    unitPickerTarget = null
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.name,
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
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
