package com.example.ui.screens

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.DateCalculatorEngine
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.ThemeMode
import com.example.ui.viewmodel.CalculatorUiState
import com.example.ui.viewmodel.CalculatorViewModel
import com.example.ui.viewmodel.DateMode
import com.example.ui.viewmodel.PercentMode
import com.example.ui.viewmodel.ToolsSubTab
import java.util.Calendar
import java.util.Locale

@Composable
fun ToolsScreen(
    state: CalculatorUiState,
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val theme = state.theme
    val subTabScrollState = rememberScrollState()
    val contentScrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        // Header
        Text(
            text = "Smart Tools",
            color = theme.textPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Sub-Tab Switcher
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(subTabScrollState)
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ToolsSubTab.values().forEach { tab ->
                val isSelected = tab == state.toolsSubTab
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (isSelected) theme.primaryAccent.copy(alpha = 0.25f)
                            else theme.surfaceGlassLight.copy(alpha = 0.4f)
                        )
                        .clickable { viewModel.setToolsSubTab(tab) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("tool_subtab_${tab.name.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab.title,
                        color = if (isSelected) theme.primaryAccent else theme.textSecondary,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // SubTab Content (Scrollable)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(contentScrollState)
                .padding(bottom = 16.dp)
        ) {
            when (state.toolsSubTab) {
                ToolsSubTab.TIP -> TipSplitterSection(state, viewModel, theme)
                ToolsSubTab.PERCENT -> PercentageCalcSection(state, viewModel, theme)
                ToolsSubTab.DATE -> DateCalcSection(state, viewModel, theme)
                ToolsSubTab.THEMES -> ThemesSection(state, viewModel, theme)
            }
        }
    }
}

// ----------------------------------------------------
// 1. Tip Splitter Section
// ----------------------------------------------------
@Composable
private fun TipSplitterSection(
    state: CalculatorUiState,
    viewModel: CalculatorViewModel,
    theme: ThemeMode
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Summary Glass Card
        LiquidGlassCard(
            theme = theme,
            modifier = Modifier.fillMaxWidth(),
            highlightIntensity = 0.6f
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Total Per Person", color = theme.textSecondary, fontSize = 13.sp)
                        Text(
                            text = "$${String.format(Locale.US, "%.2f", state.tipResult.totalPerPerson)}",
                            color = theme.primaryAccent,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Tip Per Person", color = theme.textSecondary, fontSize = 13.sp)
                        Text(
                            text = "$${String.format(Locale.US, "%.2f", state.tipResult.tipPerPerson)}",
                            color = theme.textPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(theme.surfaceGlassLight.copy(alpha = 0.4f))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total Tip: $${String.format(Locale.US, "%.2f", state.tipResult.tipAmount)}",
                        color = theme.textSecondary,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Total Bill: $${String.format(Locale.US, "%.2f", state.tipResult.totalAmount)}",
                        color = theme.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Bill Amount Input Card
        LiquidGlassCard(
            theme = theme,
            modifier = Modifier.fillMaxWidth(),
            highlightIntensity = 0.35f
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "Bill Amount ($)", color = theme.textSecondary, fontSize = 13.sp)
                OutlinedTextField(
                    value = state.tipBill,
                    onValueChange = { viewModel.setTipBill(it) },
                    shape = CircleShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = theme.textPrimary,
                        unfocusedTextColor = theme.textPrimary,
                        focusedBorderColor = theme.primaryAccent,
                        unfocusedBorderColor = Color.DarkGray
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("tip_bill_input")
                )
            }
        }

        // Tip % Preset Chips & Slider
        LiquidGlassCard(
            theme = theme,
            modifier = Modifier.fillMaxWidth(),
            highlightIntensity = 0.35f
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Tip Percentage", color = theme.textSecondary, fontSize = 13.sp)
                    Text(
                        text = "${state.tipPercent.toInt()}%",
                        color = theme.primaryAccent,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(10.0, 15.0, 18.0, 20.0, 25.0).forEach { percent ->
                        val isSelected = state.tipPercent == percent
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) theme.primaryAccent
                                    else theme.surfaceGlassLight.copy(alpha = 0.5f)
                                )
                                .clickable { viewModel.setTipPercent(percent) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${percent.toInt()}%",
                                color = if (isSelected) Color(0xFF030712) else theme.textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                // Slider
                Slider(
                    value = state.tipPercent.toFloat(),
                    onValueChange = { viewModel.setTipPercent(it.toDouble()) },
                    valueRange = 0f..50f,
                    steps = 49,
                    colors = SliderDefaults.colors(
                        thumbColor = theme.primaryAccent,
                        activeTrackColor = theme.primaryAccent,
                        inactiveTrackColor = theme.surfaceGlassLight
                    )
                )
            }
        }

        // Split Count & Round Up Row
        LiquidGlassCard(
            theme = theme,
            modifier = Modifier.fillMaxWidth(),
            highlightIntensity = 0.35f
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Split Between People", color = theme.textSecondary, fontSize = 14.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.setTipPeopleCount(state.tipPeopleCount - 1) },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(theme.surfaceGlassLight)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = theme.textPrimary)
                        }
                        Text(
                            text = "${state.tipPeopleCount}",
                            color = theme.textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        IconButton(
                            onClick = { viewModel.setTipPeopleCount(state.tipPeopleCount + 1) },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(theme.surfaceGlassLight)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", tint = theme.textPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Round Up Total", color = theme.textSecondary, fontSize = 14.sp)
                    Switch(
                        checked = state.tipRoundUp,
                        onCheckedChange = { viewModel.toggleTipRoundUp() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = theme.primaryAccent,
                            checkedTrackColor = theme.primaryAccent.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------
// 2. Percentage Calculator Section
// ----------------------------------------------------
@Composable
private fun PercentageCalcSection(
    state: CalculatorUiState,
    viewModel: CalculatorViewModel,
    theme: ThemeMode
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Mode Selector Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            PercentMode.values().forEach { mode ->
                val isSelected = state.percentMode == mode
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) theme.primaryAccent.copy(alpha = 0.25f)
                            else theme.surfaceGlassLight.copy(alpha = 0.4f)
                        )
                        .clickable { viewModel.setPercentMode(mode) }
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mode.title,
                        color = if (isSelected) theme.primaryAccent else theme.textSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        when (state.percentMode) {
            PercentMode.PERCENT_OF -> {
                LiquidGlassCard(theme = theme, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "What is X% of Y?", color = theme.primaryAccent, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = state.percentValA,
                                onValueChange = { viewModel.setPercentValA(it) },
                                label = { Text("Percent (%)") },
                                shape = CircleShape,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = theme.textPrimary,
                                    unfocusedTextColor = theme.textPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = state.percentValB,
                                onValueChange = { viewModel.setPercentValB(it) },
                                label = { Text("Total (Y)") },
                                shape = CircleShape,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = theme.textPrimary,
                                    unfocusedTextColor = theme.textPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Result: ${state.percentResult}",
                            color = theme.primaryAccent,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            PercentMode.PART_OF -> {
                LiquidGlassCard(theme = theme, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "X is what % of Y?", color = theme.primaryAccent, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = state.percentValA,
                                onValueChange = { viewModel.setPercentValA(it) },
                                label = { Text("Part (X)") },
                                shape = CircleShape,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = theme.textPrimary,
                                    unfocusedTextColor = theme.textPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = state.percentValB,
                                onValueChange = { viewModel.setPercentValB(it) },
                                label = { Text("Whole (Y)") },
                                shape = CircleShape,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = theme.textPrimary,
                                    unfocusedTextColor = theme.textPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Result: ${state.percentResult}",
                            color = theme.primaryAccent,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            PercentMode.PERCENT_CHANGE -> {
                LiquidGlassCard(theme = theme, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Percentage Change from A to B", color = theme.primaryAccent, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = state.percentValA,
                                onValueChange = { viewModel.setPercentValA(it) },
                                label = { Text("Initial (A)") },
                                shape = CircleShape,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = theme.textPrimary,
                                    unfocusedTextColor = theme.textPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = state.percentValB,
                                onValueChange = { viewModel.setPercentValB(it) },
                                label = { Text("Final (B)") },
                                shape = CircleShape,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = theme.textPrimary,
                                    unfocusedTextColor = theme.textPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Change: ${state.percentResult}",
                            color = if (state.percentResult.startsWith("-")) Color(0xFFFF5C8A) else theme.primaryAccent,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            PercentMode.DISCOUNT -> {
                LiquidGlassCard(theme = theme, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Discount & Tax Calculator", color = theme.primaryAccent, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = state.discountOriginal,
                            onValueChange = { viewModel.setDiscountInputs(it, state.discountPercent, state.discountTax) },
                            label = { Text("Original Price ($)") },
                            shape = CircleShape,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = theme.textPrimary,
                                unfocusedTextColor = theme.textPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = state.discountPercent,
                                onValueChange = { viewModel.setDiscountInputs(state.discountOriginal, it, state.discountTax) },
                                label = { Text("Discount (%)") },
                                shape = CircleShape,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = theme.textPrimary,
                                    unfocusedTextColor = theme.textPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = state.discountTax,
                                onValueChange = { viewModel.setDiscountInputs(state.discountOriginal, state.discountPercent, it) },
                                label = { Text("Tax (%)") },
                                shape = CircleShape,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = theme.textPrimary,
                                    unfocusedTextColor = theme.textPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(theme.surfaceGlassLight.copy(alpha = 0.4f))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Final Price:", color = theme.textPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("$${state.discountResult.finalPrice}", color = theme.primaryAccent, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Saved:", color = theme.textSecondary, fontSize = 13.sp)
                                Text("$${state.discountResult.totalSavings}", color = Color(0xFF06D6A0), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 3. Date Calculator Section
// ----------------------------------------------------
@Composable
private fun DateCalcSection(
    state: CalculatorUiState,
    viewModel: CalculatorViewModel,
    theme: ThemeMode
) {
    val context = LocalContext.current

    val showDatePicker = { initialMillis: Long, onSelected: (Long) -> Unit ->
        val cal = Calendar.getInstance().apply { timeInMillis = initialMillis }
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedCal = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth, 0, 0, 0)
                }
                onSelected(selectedCal.timeInMillis)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Date Mode Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DateMode.values().forEach { mode ->
                val isSelected = state.dateMode == mode
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) theme.primaryAccent.copy(alpha = 0.25f)
                            else theme.surfaceGlassLight.copy(alpha = 0.4f)
                        )
                        .clickable { viewModel.setDateMode(mode) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mode.title,
                        color = if (isSelected) theme.primaryAccent else theme.textSecondary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            }
        }

        if (state.dateMode == DateMode.DIFFERENCE) {
            LiquidGlassCard(theme = theme, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Start Date Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(theme.surfaceGlassLight.copy(alpha = 0.5f))
                            .clickable {
                                showDatePicker(state.dateStartMillis) { viewModel.setDateStart(it) }
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Start Date", color = theme.textSecondary, fontSize = 12.sp)
                            Text(
                                DateCalculatorEngine.formatDate(state.dateStartMillis),
                                color = theme.textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = theme.primaryAccent)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // End Date Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(theme.surfaceGlassLight.copy(alpha = 0.5f))
                            .clickable {
                                showDatePicker(state.dateEndMillis) { viewModel.setDateEnd(it) }
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("End Date", color = theme.textSecondary, fontSize = 12.sp)
                            Text(
                                DateCalculatorEngine.formatDate(state.dateEndMillis),
                                color = theme.textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = theme.primaryAccent)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Breakdown Result Card
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(32.dp))
                            .background(theme.surfaceGlassLight.copy(alpha = 0.4f))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "${state.dateDiffResult.totalDays} Total Days",
                            color = theme.primaryAccent,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${state.dateDiffResult.years}y ${state.dateDiffResult.months}m ${state.dateDiffResult.days}d",
                            color = theme.textPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${state.dateDiffResult.totalWeeks} weeks and ${state.dateDiffResult.remainingDaysOfWeek} days",
                            color = theme.textSecondary,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Working Days: ${state.dateDiffResult.workingDays}",
                                color = Color(0xFF06D6A0),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Weekends: ${state.dateDiffResult.weekendDays}",
                                color = theme.textSecondary,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        } else {
            // Add / Subtract Date
            LiquidGlassCard(theme = theme, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Start Date Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(theme.surfaceGlassLight.copy(alpha = 0.5f))
                            .clickable {
                                showDatePicker(state.dateStartMillis) { viewModel.setDateStart(it) }
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Base Date", color = theme.textSecondary, fontSize = 12.sp)
                            Text(
                                DateCalculatorEngine.formatDate(state.dateStartMillis),
                                color = theme.textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = theme.primaryAccent)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Add / Subtract Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(if (state.dateIsAdd) theme.primaryAccent else theme.surfaceGlassLight)
                                .clickable {
                                    viewModel.setDateAddParams(
                                        state.dateAddYears,
                                        state.dateAddMonths,
                                        state.dateAddWeeks,
                                        state.dateAddDays,
                                        true
                                    )
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+ Add Time", color = if (state.dateIsAdd) Color(0xFF030712) else theme.textPrimary, fontWeight = FontWeight.Bold)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(if (!state.dateIsAdd) Color(0xFFFF5C8A) else theme.surfaceGlassLight)
                                .clickable {
                                    viewModel.setDateAddParams(
                                        state.dateAddYears,
                                        state.dateAddMonths,
                                        state.dateAddWeeks,
                                        state.dateAddDays,
                                        false
                                    )
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("- Subtract Time", color = if (!state.dateIsAdd) Color.White else theme.textPrimary, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Inputs for Years, Months, Weeks, Days
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        DateParamField("Years", state.dateAddYears, theme, Modifier.weight(1f)) {
                            viewModel.setDateAddParams(it, state.dateAddMonths, state.dateAddWeeks, state.dateAddDays, state.dateIsAdd)
                        }
                        DateParamField("Months", state.dateAddMonths, theme, Modifier.weight(1f)) {
                            viewModel.setDateAddParams(state.dateAddYears, it, state.dateAddWeeks, state.dateAddDays, state.dateIsAdd)
                        }
                        DateParamField("Weeks", state.dateAddWeeks, theme, Modifier.weight(1f)) {
                            viewModel.setDateAddParams(state.dateAddYears, state.dateAddMonths, it, state.dateAddDays, state.dateIsAdd)
                        }
                        DateParamField("Days", state.dateAddDays, theme, Modifier.weight(1f)) {
                            viewModel.setDateAddParams(state.dateAddYears, state.dateAddMonths, state.dateAddWeeks, it, state.dateIsAdd)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Result Target Date
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(32.dp))
                            .background(theme.surfaceGlassLight.copy(alpha = 0.4f))
                            .padding(14.dp)
                    ) {
                        Text(text = "Calculated Target Date", color = theme.textSecondary, fontSize = 12.sp)
                        Text(
                            text = DateCalculatorEngine.formatDate(state.dateCalculatedTargetMillis),
                            color = theme.primaryAccent,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DateParamField(
    label: String,
    value: Int,
    theme: ThemeMode,
    modifier: Modifier = Modifier,
    onValueChange: (Int) -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = theme.textSecondary, fontSize = 11.sp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .background(theme.surfaceGlassLight)
                .padding(vertical = 4.dp, horizontal = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onValueChange((value - 1).coerceAtLeast(0)) }, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Remove, contentDescription = null, tint = theme.textPrimary, modifier = Modifier.size(14.dp))
            }
            Text("$value", color = theme.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            IconButton(onClick = { onValueChange(value + 1) }, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Add, contentDescription = null, tint = theme.textPrimary, modifier = Modifier.size(14.dp))
            }
        }
    }
}

// ----------------------------------------------------
// 4. Themes Section
// ----------------------------------------------------
@Composable
private fun ThemesSection(
    state: CalculatorUiState,
    viewModel: CalculatorViewModel,
    theme: ThemeMode
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Liquid Glass Color Themes",
            color = theme.textSecondary,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 2.dp)
        )

        ThemeMode.values().forEach { mode ->
            val isSelected = state.theme == mode

            LiquidGlassCard(
                theme = theme,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.setTheme(mode) }
                    .testTag("theme_card_${mode.name.lowercase()}"),
                highlightIntensity = if (isSelected) 0.8f else 0.3f
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Visual gradient preview orb
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(mode.primaryAccent, mode.secondaryAccent, mode.tertiaryAccent)
                                    )
                                )
                                .border(2.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = mode.title,
                                color = theme.textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                            Text(
                                text = mode.description,
                                color = theme.textSecondary.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(mode.primaryAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Active theme",
                                tint = Color(0xFF030712),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
