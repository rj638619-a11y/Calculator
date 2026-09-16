package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.engine.DateCalculatorEngine
import com.example.engine.ExpressionEvaluator
import com.example.engine.FinanceAndTipEngine
import com.example.engine.UnitCategory
import com.example.engine.UnitConversionEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.ui.components.CalcButtonType
import com.example.ui.components.LiquidGlassButton
import com.example.ui.theme.ThemeMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun `test liquid glass button content description semantics`() {
    composeTestRule.setContent {
      LiquidGlassButton(
        text = "÷",
        onClick = {},
        theme = ThemeMode.LIGHT,
        type = CalcButtonType.OPERATOR,
        testTag = "btn_divide",
        contentDescription = "Divide"
      )
    }

    composeTestRule
      .onNodeWithTag("btn_divide")
      .assertContentDescriptionEquals("Divide")
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Calculator", appName)
  }

  @Test
  fun `test expression evaluator basic and scientific operations`() {
    val res1 = ExpressionEvaluator.evaluate("2 + 3 * 4", ExpressionEvaluator.AngleMode.DEG)
    assertTrue(res1 is ExpressionEvaluator.EvalResult.Success)
    assertEquals("14", (res1 as ExpressionEvaluator.EvalResult.Success).formatted)

    val res2 = ExpressionEvaluator.evaluate("sin(90) + cos(0)", ExpressionEvaluator.AngleMode.DEG)
    assertTrue(res2 is ExpressionEvaluator.EvalResult.Success)
    assertEquals("2", (res2 as ExpressionEvaluator.EvalResult.Success).formatted)

    val res3 = ExpressionEvaluator.evaluate("5!", ExpressionEvaluator.AngleMode.DEG)
    assertTrue(res3 is ExpressionEvaluator.EvalResult.Success)
    assertEquals("120", (res3 as ExpressionEvaluator.EvalResult.Success).formatted)
  }

  @Test
  fun `test unit converter length conversion`() {
    val lengthUnits = UnitConversionEngine.UNITS[UnitCategory.LENGTH]!!
    val meter = lengthUnits[0] // Meter
    val km = lengthUnits[1]    // Kilometer

    val converted = UnitConversionEngine.convert(1000.0, meter, km)
    assertEquals(1.0, converted, 0.0001)
  }

  @Test
  fun `test tip calculation`() {
    val tip = FinanceAndTipEngine.calculateTip(billAmount = 100.0, tipPercent = 15.0, splitCount = 2, roundUp = false)
    assertEquals(15.0, tip.tipAmount, 0.01)
    assertEquals(115.0, tip.totalAmount, 0.01)
    assertEquals(57.5, tip.totalPerPerson, 0.01)
  }
}
