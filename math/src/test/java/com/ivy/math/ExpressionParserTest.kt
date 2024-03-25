package com.ivy.math

import assertk.assertThat
import assertk.assertions.isEqualTo
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


}