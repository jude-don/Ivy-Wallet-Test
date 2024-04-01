package com.ivy.math

import arrow.core.left
import arrow.core.nonEmptyListOf
import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MultiplyTest{
    private lateinit var multiply: Multiply

    @BeforeEach
    fun setUp(){
        multiply = Multiply(
            left= TreeNodeFake(3.0),
            right=TreeNodeFake(4.0)
        )
    }

    @Test
    fun `Testing multiply print function`(){
        val result = multiply.print()

        assertThat(result).isEqualTo("(3.0*4.0.)")
    }

    @Test
    fun `Testing multiply eval function`(){
        val result = multiply.eval()

        assertThat(result).isEqualTo(12.0)
    }
}