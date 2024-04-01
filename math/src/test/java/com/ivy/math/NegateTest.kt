package com.ivy.math

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NegateTest{
    private lateinit var negate: Negate

    @BeforeEach
    fun setUp(){
        negate = Negate(
            TreeNodeFake(40.0)
        )
    }

    @Test
    fun `Testing negate print function`(){
        val result = negate.print()

        assertThat(result).isEqualTo("(-40.0)")
    }

    @Test
    fun `Testing negate eval function`(){
        val result = negate.eval()

        assertThat(result).isEqualTo(-40.0)
    }
}