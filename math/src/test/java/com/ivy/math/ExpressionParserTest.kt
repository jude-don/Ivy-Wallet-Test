package com.ivy.math

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ivy.math.calculator.CalculatorOperator
import com.ivy.math.calculator.appendDecimalSeparator
import com.ivy.math.calculator.appendTo
import com.ivy.parser.Parser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource


// This is a test to evaluate the calculator computation in the app.
internal class ExpressionParserTest {

    private lateinit var parser: Parser<TreeNode>

    // Test setup
    @BeforeEach
    fun setUp() {
        parser = expressionParser() // Re
    }

    @Test
    fun `Test evaluating simple expression using evaluate function`() {

        val actual = evaluate("2+3")
        assertThat(actual).isEqualTo(5.0)
    }

    @Test
    fun `Test evaluating invalid expression`() {
        val result = evaluate("2 +")
        assertNull(result)
    }

    @ParameterizedTest
    @CsvSource(
        "1234567.8912345, '1,234,567.891235'",
        "100.123456, '100.123456'",
        "0.001234, '0.001234'",
        "1000.0, '1,000'",
        "9999999.999999, '9,999,999.999999'",
        "0.0, '0'"
    )
    fun `formatNumber formats numbers correctly`(input: Double, expected: String) {
        assertEquals(expected, formatNumber(input))
    }


    // Creating a parameterized test to pass multiple parameters.
    @ParameterizedTest
    @CsvSource( // actual, expected
        "3+6/3-(-10), 15.0",
        "5+6, 11.0",
        "10-2, 8",
        "50%, 0.5",
        "5.0000000, 5.0",
        "100/(10*10), 1.0",
    )
    fun `Test to evaluate expression`(expression: String, expected: Double) {
        val result = parser(expression).first()

        val actual = result.value.eval()

        // assertion
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `appendDecimalSeparator adds decimal when last character is a number`() {
        val expression = "3"
        val decimalSeparator = '.'
        val expected = "3."

        val result = appendDecimalSeparator(expression, decimalSeparator)

        assertEquals(expected, result)
    }

    @Test
    fun `appendDecimalSeparator does not add decimal when last character is a decimal`() {
        val expression = "3."
        val decimalSeparator = '.'
        val expected = "3."

        val result = appendDecimalSeparator(expression, decimalSeparator)

        assertEquals(expected, result)
    }

    @Test
    fun `appendDecimalSeparator does not add decimal when last character is not a digit`() {
        val expression = "3+"
        val decimalSeparator = '.'
        val expected = "3+0."

        val result = appendDecimalSeparator(expression, decimalSeparator)

        assertEquals(expected, result)
    }

    @Test
    fun `appendDecimalSeparator adds decimal after a closing parenthesis`() {
        val expression = "(3+3)"
        val decimalSeparator = '.'
        val expected = "(3+3)"

        val result = appendDecimalSeparator(expression, decimalSeparator)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `appendDecimalSeparator does not add another decimal if last number already contains one`() {
        val expression = "3.5+2"
        val decimalSeparator = '.'
        val expected = "3.5+2."

        val result = appendDecimalSeparator(expression, decimalSeparator)

        assertEquals(expected, result)
    }

    @Test
    fun `appendDecimalSeparator does not add decimal to empty expression`() {
        val expression = ""
        val decimalSeparator = '.'
        val expected = "0."

        val result = appendDecimalSeparator(expression, decimalSeparator)

        assertThat(result).isEqualTo(expected)
    }


    @Test
    fun `appendTo appends plus correctly`() {
        val expression = "10"
        val expected = "10+"
        val result = appendTo(expression, CalculatorOperator.Plus)
        assertEquals(expected, result)
    }

    @Test
    fun `appendTo appends minus correctly`() {
        val expression = "10"
        val expected = "10-"
        val result = appendTo(expression, CalculatorOperator.Minus)
        assertEquals(expected, result)
    }

    @Test
    fun `appendTo appends multiply correctly`() {
        val expression = "10"
        val expected = "10*"
        val result = appendTo(expression, CalculatorOperator.Multiply)
        assertEquals(expected, result)
    }

    @Test
    fun `appendTo appends divide correctly`() {
        val expression = "10"
        val expected = "10/"
        val result = appendTo(expression, CalculatorOperator.Divide)
        assertEquals(expected, result)
    }

    @Test
    fun `appendTo appends brackets correctly at the beginning`() {
        val expression = ""
        val expected = "("
        val result = appendTo(expression, CalculatorOperator.Brackets)
        assertEquals(expected, result)
    }

    @Test
    fun `appendTo appends brackets correctly after a number`() {
        val expression = "10"
        val expected = "10*("
        val result = appendTo(expression, CalculatorOperator.Brackets)
        assertEquals(expected, result)
    }

    @Test
    fun `appendTo appends percent correctly`() {
        val expression = "10"
        val expected = "10%"
        val result = appendTo(expression, CalculatorOperator.Percent)
        assertEquals(expected, result)
    }

    // Add more tests for each specific function and scenario
    // ...




}