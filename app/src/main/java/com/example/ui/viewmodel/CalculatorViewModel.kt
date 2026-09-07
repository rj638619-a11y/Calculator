package com.example.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.CalculationEntity
import com.example.data.local.CalculationRepository
import com.example.data.remote.CurrencyData
import com.example.data.remote.CurrencyRepository
import com.example.engine.DateCalculatorEngine
import com.example.engine.DateDiffResult
import com.example.engine.DiscountResult
import com.example.engine.ExpressionEvaluator
import com.example.engine.FinanceAndTipEngine
import com.example.engine.TipResult
import com.example.engine.UnitCategory
import com.example.engine.UnitConversionEngine
import com.example.engine.UnitItem
import com.example.ui.components.AppNavTab
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

data class CalculatorUiState(
    val currentTab: AppNavTab = AppNavTab.CALCULATOR,
    val theme: ThemeMode = ThemeMode.AURORA,
    
    // Calculator
    val expression: String = "",
    val liveResult: String = "",
    val evaluatedResult: String = "",
    val angleMode: ExpressionEvaluator.AngleMode = ExpressionEvaluator.AngleMode.DEG,
    val isScientificExpanded: Boolean = false,
    val memoryValue: Double = 0.0,
    val hasMemory: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",

    // Currency
    val fromCurrency: String = "USD",
    val toCurrency: String = "EUR",
    val currencyAmount: String = "100",
    val convertedCurrencyAmount: Double = 92.0,
    val currencyRates: Map<String, Double> = CurrencyData.DEFAULT_RATES,
    val currencyLastUpdated: String = "Rates loaded",
    val isCurrencyLoading: Boolean = false,

    // Unit Converter
    val unitCategory: UnitCategory = UnitCategory.LENGTH,
    val fromUnit: UnitItem = UnitConversionEngine.UNITS[UnitCategory.LENGTH]!![0], // Meter
    val toUnit: UnitItem = UnitConversionEngine.UNITS[UnitCategory.LENGTH]!![1],   // Kilometer
    val unitInputValue: String = "1",
    val unitOutputValue: String = "0.001",

    // Tools - Active subtab
    val toolsSubTab: ToolsSubTab = ToolsSubTab.TIP,

    // Tip Calculator
    val tipBill: String = "50",
    val tipPercent: Double = 15.0,
    val tipPeopleCount: Int = 2,
    val tipRoundUp: Boolean = false,
    val tipResult: TipResult = TipResult(7.5, 57.5, 3.75, 28.75),

    // Percentage Calculator
    val percentMode: PercentMode = PercentMode.PERCENT_OF,
    val percentValA: String = "20",
    val percentValB: String = "150",
    val percentResult: String = "30",
    val discountOriginal: String = "120",
    val discountPercent: String = "25",
    val discountTax: String = "8",
    val discountResult: DiscountResult = DiscountResult(30.0, 90.0, 7.2, 97.2, 30.0),

    // Date Calculator
    val dateMode: DateMode = DateMode.DIFFERENCE,
    val dateStartMillis: Long = System.currentTimeMillis(),
    val dateEndMillis: Long = System.currentTimeMillis() + 86400000L * 30,
    val dateDiffResult: DateDiffResult = DateCalculatorEngine.calculateDifference(
        System.currentTimeMillis(),
        System.currentTimeMillis() + 86400000L * 30
    ),
    val dateAddYears: Int = 0,
    val dateAddMonths: Int = 1,
    val dateAddWeeks: Int = 0,
    val dateAddDays: Int = 0,
    val dateIsAdd: Boolean = true,
    val dateCalculatedTargetMillis: Long = System.currentTimeMillis() + 86400000L * 30
)

enum class ToolsSubTab(val title: String) {
    TIP("Tip Splitter"),
    PERCENT("Percentages"),
    DATE("Date Calc"),
    THEMES("Themes")
}

enum class PercentMode(val title: String) {
    PERCENT_OF("X% of Y"),
    PART_OF("X is what % of Y"),
    PERCENT_CHANGE("% Change"),
    DISCOUNT("Discount & Tax")
}

enum class DateMode(val title: String) {
    DIFFERENCE("Date Difference"),
    ADD_SUBTRACT("Add / Subtract Date")
}

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val calculationRepository = CalculationRepository(db.calculationDao())
    private val currencyRepository = CurrencyRepository()
    private val clipboardManager = application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    val historyList: StateFlow<List<CalculationEntity>> = calculationRepository.allHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Initial live rate fetch
        refreshCurrencyRates()
        // Initialize tool calculations
        updateTipCalculation()
        updatePercentageCalculation()
        updateDateCalculation()
    }

    // ----------------------------------------------------
    // Tab & Theme Navigation
    // ----------------------------------------------------
    fun selectTab(tab: AppNavTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun setTheme(theme: ThemeMode) {
        _uiState.update { it.copy(theme = theme) }
    }

    fun setToolsSubTab(subTab: ToolsSubTab) {
        _uiState.update { it.copy(toolsSubTab = subTab) }
    }

    // ----------------------------------------------------
    // Calculator Operations
    // ----------------------------------------------------
    fun toggleScientific() {
        _uiState.update { it.copy(isScientificExpanded = !it.isScientificExpanded) }
    }

    fun toggleAngleMode() {
        _uiState.update {
            val newMode = if (it.angleMode == ExpressionEvaluator.AngleMode.DEG)
                ExpressionEvaluator.AngleMode.RAD
            else ExpressionEvaluator.AngleMode.DEG
            it.copy(angleMode = newMode)
        }
        recomputeLiveResult()
    }

    fun onInput(charOrFunc: String) {
        val currentExp = _uiState.value.expression
        val newExp = when (charOrFunc) {
            "sin", "cos", "tan", "asin", "acos", "atan", "log", "ln", "sqrt", "cbrt", "abs" -> {
                "$currentExp$charOrFunc("
            }
            "1/x" -> {
                if (currentExp.isEmpty()) "1/(" else "$currentExp*(1/("
            }
            "x²" -> "$currentExp^2"
            "x³" -> "$currentExp^3"
            "x^y" -> "$currentExp^"
            "+/-" -> {
                if (currentExp.startsWith("-(")) {
                    currentExp.removePrefix("-(").removeSuffix(")")
                } else {
                    "-($currentExp)"
                }
            }
            else -> "$currentExp$charOrFunc"
        }

        _uiState.update { it.copy(expression = newExp, isError = false, errorMessage = "") }
        recomputeLiveResult()
    }

    fun onBackspace() {
        val currentExp = _uiState.value.expression
        if (currentExp.isNotEmpty()) {
            val updated = when {
                currentExp.endsWith("sqrt(") || currentExp.endsWith("cbrt(") || currentExp.endsWith("asin(") ||
                        currentExp.endsWith("acos(") || currentExp.endsWith("atan(") -> {
                    currentExp.dropLast(5)
                }
                currentExp.endsWith("sin(") || currentExp.endsWith("cos(") || currentExp.endsWith("tan(") ||
                        currentExp.endsWith("log(") || currentExp.endsWith("abs(") -> {
                    currentExp.dropLast(4)
                }
                currentExp.endsWith("ln(") -> currentExp.dropLast(3)
                else -> currentExp.dropLast(1)
            }
            _uiState.update { it.copy(expression = updated, isError = false, errorMessage = "") }
            recomputeLiveResult()
        }
    }

    fun onClear() {
        _uiState.update {
            it.copy(
                expression = "",
                liveResult = "",
                evaluatedResult = "",
                isError = false,
                errorMessage = ""
            )
        }
    }

    fun onEquals() {
        val currentExp = _uiState.value.expression
        if (currentExp.isBlank()) return

        when (val result = ExpressionEvaluator.evaluate(currentExp, _uiState.value.angleMode)) {
            is ExpressionEvaluator.EvalResult.Success -> {
                _uiState.update {
                    it.copy(
                        evaluatedResult = result.formatted,
                        liveResult = "",
                        isError = false,
                        errorMessage = ""
                    )
                }
                // Save to Room DB
                viewModelScope.launch {
                    val calcType = if (_uiState.value.isScientificExpanded) "SCIENTIFIC" else "BASIC"
                    calculationRepository.insert(currentExp, result.formatted, calcType)
                }
            }
            is ExpressionEvaluator.EvalResult.Error -> {
                _uiState.update {
                    it.copy(
                        isError = true,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    private fun recomputeLiveResult() {
        val exp = _uiState.value.expression
        if (exp.isBlank()) {
            _uiState.update { it.copy(liveResult = "") }
            return
        }

        when (val eval = ExpressionEvaluator.evaluate(exp, _uiState.value.angleMode)) {
            is ExpressionEvaluator.EvalResult.Success -> {
                _uiState.update { it.copy(liveResult = eval.formatted) }
            }
            is ExpressionEvaluator.EvalResult.Error -> {
                // If it's a partial expression being typed, don't show full error in live preview
                _uiState.update { it.copy(liveResult = "") }
            }
        }
    }

    // Memory operations (MC, MR, M+, M-, MS)
    fun memoryClear() {
        _uiState.update { it.copy(memoryValue = 0.0, hasMemory = false) }
        showToast("Memory cleared")
    }

    fun memoryRecall() {
        if (_uiState.value.hasMemory) {
            val memFormatted = ExpressionEvaluator.formatResult(_uiState.value.memoryValue)
            onInput(memFormatted)
        }
    }

    fun memoryAdd() {
        val currentVal = getCurrentEvaluationValue()
        val newMem = _uiState.value.memoryValue + currentVal
        _uiState.update { it.copy(memoryValue = newMem, hasMemory = true) }
        showToast("Added to Memory: ${ExpressionEvaluator.formatResult(newMem)}")
    }

    fun memorySubtract() {
        val currentVal = getCurrentEvaluationValue()
        val newMem = _uiState.value.memoryValue - currentVal
        _uiState.update { it.copy(memoryValue = newMem, hasMemory = true) }
        showToast("Subtracted from Memory: ${ExpressionEvaluator.formatResult(newMem)}")
    }

    fun memoryStore() {
        val currentVal = getCurrentEvaluationValue()
        _uiState.update { it.copy(memoryValue = currentVal, hasMemory = true) }
        showToast("Stored in Memory: ${ExpressionEvaluator.formatResult(currentVal)}")
    }

    private fun getCurrentEvaluationValue(): Double {
        val exp = _uiState.value.expression
        if (exp.isNotBlank()) {
            when (val eval = ExpressionEvaluator.evaluate(exp, _uiState.value.angleMode)) {
                is ExpressionEvaluator.EvalResult.Success -> return eval.value
                else -> {}
            }
        }
        return _uiState.value.evaluatedResult.toDoubleOrNull() ?: 0.0
    }

    // ----------------------------------------------------
    // History Actions
    // ----------------------------------------------------
    fun reuseHistory(item: CalculationEntity, asExpression: Boolean) {
        if (asExpression) {
            _uiState.update {
                it.copy(
                    expression = item.expression,
                    currentTab = AppNavTab.CALCULATOR,
                    isError = false
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    expression = item.result,
                    currentTab = AppNavTab.CALCULATOR,
                    isError = false
                )
            }
        }
        recomputeLiveResult()
        showToast("Loaded calculation")
    }

    fun deleteHistoryItem(entity: CalculationEntity) {
        viewModelScope.launch {
            calculationRepository.delete(entity)
            showToast("Calculation deleted")
        }
    }

    fun toggleFavoriteHistory(entity: CalculationEntity) {
        viewModelScope.launch {
            calculationRepository.toggleFavorite(entity.id, entity.isFavorite)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            calculationRepository.clearAll()
            showToast("History cleared")
        }
    }

    // ----------------------------------------------------
    // Currency Converter Operations
    // ----------------------------------------------------
    fun refreshCurrencyRates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCurrencyLoading = true) }
            val result = currencyRepository.fetchRates("USD")
            result.onSuccess { (rates, lastUpdated) ->
                _uiState.update {
                    it.copy(
                        currencyRates = rates,
                        currencyLastUpdated = lastUpdated,
                        isCurrencyLoading = false
                    )
                }
                recalculateCurrency()
            }.onFailure {
                _uiState.update { it.copy(isCurrencyLoading = false) }
            }
        }
    }

    fun setFromCurrency(code: String) {
        _uiState.update { it.copy(fromCurrency = code) }
        recalculateCurrency()
    }

    fun setToCurrency(code: String) {
        _uiState.update { it.copy(toCurrency = code) }
        recalculateCurrency()
    }

    fun swapCurrencies() {
        _uiState.update {
            it.copy(
                fromCurrency = it.toCurrency,
                toCurrency = it.fromCurrency
            )
        }
        recalculateCurrency()
    }

    fun setCurrencyAmount(amount: String) {
        _uiState.update { it.copy(currencyAmount = amount) }
        recalculateCurrency()
    }

    private fun recalculateCurrency() {
        val amount = _uiState.value.currencyAmount.toDoubleOrNull() ?: 0.0
        val converted = currencyRepository.convert(
            amount = amount,
            fromCode = _uiState.value.fromCurrency,
            toCode = _uiState.value.toCurrency,
            rates = _uiState.value.currencyRates
        )
        _uiState.update { it.copy(convertedCurrencyAmount = converted) }
    }

    // ----------------------------------------------------
    // Unit Converter Operations
    // ----------------------------------------------------
    fun selectUnitCategory(category: UnitCategory) {
        val units = UnitConversionEngine.UNITS[category] ?: return
        val from = units.firstOrNull() ?: return
        val to = units.getOrNull(1) ?: from

        _uiState.update {
            it.copy(
                unitCategory = category,
                fromUnit = from,
                toUnit = to
            )
        }
        recalculateUnit()
    }

    fun setFromUnit(unit: UnitItem) {
        _uiState.update { it.copy(fromUnit = unit) }
        recalculateUnit()
    }

    fun setToUnit(unit: UnitItem) {
        _uiState.update { it.copy(toUnit = unit) }
        recalculateUnit()
    }

    fun swapUnits() {
        _uiState.update {
            it.copy(
                fromUnit = it.toUnit,
                toUnit = it.fromUnit
            )
        }
        recalculateUnit()
    }

    fun setUnitInputValue(value: String) {
        _uiState.update { it.copy(unitInputValue = value) }
        recalculateUnit()
    }

    private fun recalculateUnit() {
        val input = _uiState.value.unitInputValue.toDoubleOrNull() ?: 0.0
        val converted = UnitConversionEngine.convert(
            value = input,
            fromUnit = _uiState.value.fromUnit,
            toUnit = _uiState.value.toUnit
        )
        _uiState.update {
            it.copy(unitOutputValue = UnitConversionEngine.formatUnitValue(converted))
        }
    }

    // ----------------------------------------------------
    // Tip Splitter Operations
    // ----------------------------------------------------
    fun setTipBill(bill: String) {
        _uiState.update { it.copy(tipBill = bill) }
        updateTipCalculation()
    }

    fun setTipPercent(percent: Double) {
        _uiState.update { it.copy(tipPercent = percent) }
        updateTipCalculation()
    }

    fun setTipPeopleCount(count: Int) {
        val validCount = count.coerceIn(1, 100)
        _uiState.update { it.copy(tipPeopleCount = validCount) }
        updateTipCalculation()
    }

    fun toggleTipRoundUp() {
        _uiState.update { it.copy(tipRoundUp = !it.tipRoundUp) }
        updateTipCalculation()
    }

    private fun updateTipCalculation() {
        val bill = _uiState.value.tipBill.toDoubleOrNull() ?: 0.0
        val result = FinanceAndTipEngine.calculateTip(
            billAmount = bill,
            tipPercent = _uiState.value.tipPercent,
            splitCount = _uiState.value.tipPeopleCount,
            roundUp = _uiState.value.tipRoundUp
        )
        _uiState.update { it.copy(tipResult = result) }
    }

    // ----------------------------------------------------
    // Percentage Calculator Operations
    // ----------------------------------------------------
    fun setPercentMode(mode: PercentMode) {
        _uiState.update { it.copy(percentMode = mode) }
        updatePercentageCalculation()
    }

    fun setPercentValA(valA: String) {
        _uiState.update { it.copy(percentValA = valA) }
        updatePercentageCalculation()
    }

    fun setPercentValB(valB: String) {
        _uiState.update { it.copy(percentValB = valB) }
        updatePercentageCalculation()
    }

    fun setDiscountInputs(original: String, discount: String, tax: String) {
        _uiState.update {
            it.copy(
                discountOriginal = original,
                discountPercent = discount,
                discountTax = tax
            )
        }
        updatePercentageCalculation()
    }

    private fun updatePercentageCalculation() {
        val a = _uiState.value.percentValA.toDoubleOrNull() ?: 0.0
        val b = _uiState.value.percentValB.toDoubleOrNull() ?: 0.0

        val res = when (_uiState.value.percentMode) {
            PercentMode.PERCENT_OF -> "${FinanceAndTipEngine.calculatePercentageOf(a, b)}"
            PercentMode.PART_OF -> "${FinanceAndTipEngine.calculatePartOfTotal(a, b)}%"
            PercentMode.PERCENT_CHANGE -> {
                val change = FinanceAndTipEngine.calculatePercentageChange(a, b)
                val sign = if (change > 0) "+" else ""
                "$sign$change%"
            }
            PercentMode.DISCOUNT -> ""
        }

        val orig = _uiState.value.discountOriginal.toDoubleOrNull() ?: 0.0
        val disc = _uiState.value.discountPercent.toDoubleOrNull() ?: 0.0
        val tax = _uiState.value.discountTax.toDoubleOrNull() ?: 0.0
        val discRes = FinanceAndTipEngine.calculateDiscountAndTax(orig, disc, tax)

        _uiState.update {
            it.copy(
                percentResult = res,
                discountResult = discRes
            )
        }
    }

    // ----------------------------------------------------
    // Date Calculator Operations
    // ----------------------------------------------------
    fun setDateMode(mode: DateMode) {
        _uiState.update { it.copy(dateMode = mode) }
        updateDateCalculation()
    }

    fun setDateStart(millis: Long) {
        _uiState.update { it.copy(dateStartMillis = millis) }
        updateDateCalculation()
    }

    fun setDateEnd(millis: Long) {
        _uiState.update { it.copy(dateEndMillis = millis) }
        updateDateCalculation()
    }

    fun setDateAddParams(years: Int, months: Int, weeks: Int, days: Int, isAdd: Boolean) {
        _uiState.update {
            it.copy(
                dateAddYears = years,
                dateAddMonths = months,
                dateAddWeeks = weeks,
                dateAddDays = days,
                dateIsAdd = isAdd
            )
        }
        updateDateCalculation()
    }

    private fun updateDateCalculation() {
        val diff = DateCalculatorEngine.calculateDifference(
            _uiState.value.dateStartMillis,
            _uiState.value.dateEndMillis
        )

        val target = DateCalculatorEngine.modifyDate(
            baseMillis = _uiState.value.dateStartMillis,
            years = _uiState.value.dateAddYears,
            months = _uiState.value.dateAddMonths,
            weeks = _uiState.value.dateAddWeeks,
            days = _uiState.value.dateAddDays,
            isAdd = _uiState.value.dateIsAdd
        )

        _uiState.update {
            it.copy(
                dateDiffResult = diff,
                dateCalculatedTargetMillis = target
            )
        }
    }

    // ----------------------------------------------------
    // Clipboard Utility
    // ----------------------------------------------------
    fun copyToClipboard(text: String, label: String = "Smart Calculator") {
        if (text.isNotBlank()) {
            val clip = ClipData.newPlainText(label, text)
            clipboardManager.setPrimaryClip(clip)
            showToast("Copied to clipboard: $text")
        }
    }

    private fun showToast(message: String) {
        viewModelScope.launch {
            _toastEvent.emit(message)
        }
    }
}
