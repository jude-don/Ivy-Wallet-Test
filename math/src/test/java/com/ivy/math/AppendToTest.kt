package com.ivy.math

import com.ivy.math.calculator.appendTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AppendToTest {

    @Test
    fun `appendTo should append digit directly when last character is not ) or %`() {
        val expression = "123+"
        val digit = 4
        val result = appendTo(expression, digit)
        assertEquals("123+4", result, "Digit should be appended directly")
    }

    @Test
    fun `appendTo should append digit with * when last character is )`() {
        val expression = "(123+456)"
        val digit = 7
        val result = appendTo(expression, digit)
        assertEquals("(123+456)*7", result, "Digit should be appended with * when last character is )")
    }

    @Test
    fun `appendTo should append digit with * when last character is %`() {
        val expression = "100%"
        val digit = 5
        val result = appendTo(expression, digit)
        assertEquals("100%*5", result, "Digit should be appended with * when last character is %")
    }

    @Test
    fun `appendTo should work with empty expression`() {
        val expression = ""
        val digit = 1
        val result = appendTo(expression, digit)
        assertEquals("1", result, "Digit should be appended even if expression is empty")
    }
}