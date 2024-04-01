package com.ivy.math

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DivideTest{
    private lateinit var divide: Divide

    @BeforeEach
    fun setUp(){
        divide = Divide(
            left= TreeNodeFake(12.0),
            right=TreeNodeFake(4.0)
        )
    }

    @Test
    fun `Test divide print function`(){
        val result = divide.print()

        assertThat(result).isEqualTo("(12.0/4.0.)")
    }

    @Test
    fun `Test divide eval function`(){
        val result =  divide.eval()

        assertThat(result).isEqualTo(3.0)
    }
}