package com.ivy.math

import com.ivy.math.calculator.beautify
import com.ivy.math.calculator.hasObviousResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class ExpressionUtilTest {

    @Test
    fun `hasObviousResult should return true for expression ending with operator without preceding calculations`() {
        assertEquals(true, hasObviousResult("123+", null))
    }

    @Test
    fun `hasObviousResult should return false for expression ending with operator with preceding calculations`() {
        assertEquals(false, hasObviousResult("123+456-", null))
    }

    @Test
    fun `hasObviousResult should return true for expression equal to provided value`() {
        assertEquals(true, hasObviousResult("123", 123.0))
    }

    @Test
    fun `hasObviousResult should return false for expression not equal to provided value`() {
        assertEquals(false, hasObviousResult("123", 456.0))
    }

    @Test
    fun `beautify should format integers correctly`() {
        assertEquals("1,234", beautify("1234"))
    }

    @Test
    fun `beautify should format decimals correctly`() {
        assertEquals("1,234.56", beautify("1234.56"))
    }

    @Test
    fun `beautify should handle expressions with multiple operations and numbers`() {
        assertEquals("1,234+5,678.90", beautify("1234+5678.90"))
    }

    @Test
    fun `beautify should return null for empty expressions`() {
        assertNull(beautify(""))
    }

    @Test
    fun `beautify should not alter expressions without numbers`() {
        assertEquals("+", beautify("+"))
    }



}