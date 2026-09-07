package com.example

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.components.AppNavTab
import com.example.ui.components.LiquidGlassNavBar
import com.example.ui.screens.CalculatorScreen
import com.example.ui.screens.CurrencyScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.ToolsScreen
import com.example.ui.screens.UnitConverterScreen
import com.example.ui.theme.LiquidGlassBackground
import com.example.ui.theme.SmartCalculatorTheme
import com.example.ui.viewmodel.CalculatorViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val state by viewModel.uiState.collectAsState()
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(Unit) {
                viewModel.toastEvent.collectLatest { message ->
                    snackbarHostState.showSnackbar(message)
                }
            }

            SmartCalculatorTheme(themeMode = state.theme) {
                LiquidGlassBackground(theme = state.theme) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = Color.Transparent,
                        contentColor = state.theme.textPrimary,
                        snackbarHost = {
                            SnackbarHost(
                                hostState = snackbarHostState,
                                modifier = Modifier.padding(bottom = 80.dp)
                            )
                        },
                        bottomBar = {
                            LiquidGlassNavBar(
                                selectedTab = state.currentTab,
                                onTabSelected = { viewModel.selectTab(it) },
                                theme = state.theme
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .windowInsetsPadding(WindowInsets.statusBars)
                        ) {
                            AnimatedContent(
                                targetState = state.currentTab,
                                transitionSpec = {
                                    fadeIn() togetherWith fadeOut()
                                },
                                label = "TabTransition"
                            ) { tab ->
                                when (tab) {
                                    AppNavTab.CALCULATOR -> CalculatorScreen(
                                        state = state,
                                        viewModel = viewModel
                                    )
                                    AppNavTab.CURRENCY -> CurrencyScreen(
                                        state = state,
                                        viewModel = viewModel
                                    )
                                    AppNavTab.UNITS -> UnitConverterScreen(
                                        state = state,
                                        viewModel = viewModel
                                    )
                                    AppNavTab.TOOLS -> ToolsScreen(
                                        state = state,
                                        viewModel = viewModel
                                    )
                                    AppNavTab.HISTORY -> HistoryScreen(
                                        viewModel = viewModel,
                                        theme = state.theme
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Hardware Keyboard Support
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event == null) return super.onKeyDown(keyCode, event)

        if (viewModel.uiState.value.currentTab == AppNavTab.CALCULATOR) {
            when (keyCode) {
                KeyEvent.KEYCODE_0, KeyEvent.KEYCODE_NUMPAD_0 -> { viewModel.onInput("0"); return true }
                KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_NUMPAD_1 -> { viewModel.onInput("1"); return true }
                KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_NUMPAD_2 -> { viewModel.onInput("2"); return true }
                KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_NUMPAD_3 -> { viewModel.onInput("3"); return true }
                KeyEvent.KEYCODE_4, KeyEvent.KEYCODE_NUMPAD_4 -> { viewModel.onInput("4"); return true }
                KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_NUMPAD_5 -> {
                    if (event.isShiftPressed) viewModel.onInput("%") else viewModel.onInput("5")
                    return true
                }
                KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_NUMPAD_6 -> {
                    if (event.isShiftPressed) viewModel.onInput("^") else viewModel.onInput("6")
                    return true
                }
                KeyEvent.KEYCODE_7, KeyEvent.KEYCODE_NUMPAD_7 -> { viewModel.onInput("7"); return true }
                KeyEvent.KEYCODE_8, KeyEvent.KEYCODE_NUMPAD_8 -> {
                    if (event.isShiftPressed) viewModel.onInput("×") else viewModel.onInput("8")
                    return true
                }
                KeyEvent.KEYCODE_9, KeyEvent.KEYCODE_NUMPAD_9 -> {
                    if (event.isShiftPressed) viewModel.onInput("(") else viewModel.onInput("9")
                    return true
                }
                KeyEvent.KEYCODE_NUMPAD_DOT, KeyEvent.KEYCODE_PERIOD -> { viewModel.onInput("."); return true }
                KeyEvent.KEYCODE_PLUS, KeyEvent.KEYCODE_NUMPAD_ADD -> { viewModel.onInput("+"); return true }
                KeyEvent.KEYCODE_MINUS, KeyEvent.KEYCODE_NUMPAD_SUBTRACT -> { viewModel.onInput("-"); return true }
                KeyEvent.KEYCODE_STAR, KeyEvent.KEYCODE_NUMPAD_MULTIPLY -> { viewModel.onInput("×"); return true }
                KeyEvent.KEYCODE_SLASH, KeyEvent.KEYCODE_NUMPAD_DIVIDE -> { viewModel.onInput("÷"); return true }
                KeyEvent.KEYCODE_EQUALS, KeyEvent.KEYCODE_NUMPAD_EQUALS, KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_NUMPAD_ENTER -> {
                    viewModel.onEquals()
                    return true
                }
                KeyEvent.KEYCODE_DEL -> { viewModel.onBackspace(); return true }
                KeyEvent.KEYCODE_ESCAPE, KeyEvent.KEYCODE_C -> {
                    if (keyCode == KeyEvent.KEYCODE_ESCAPE || (keyCode == KeyEvent.KEYCODE_C && !event.isCtrlPressed)) {
                        viewModel.onClear()
                        return true
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
