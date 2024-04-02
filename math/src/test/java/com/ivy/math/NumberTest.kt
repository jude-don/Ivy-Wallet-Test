package com.ivy.math

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import com.ivy.math.Number
import org.junit.jupiter.api.Test

internal class NumberTest{
    private lateinit var number: Number

    @BeforeEach
    fun setUp(){
        number = Number(40.0)
    }

    @Test
    fun `Testing number print function`(){
       val result = number.print()

        assertThat(result).isEqualTo("40.0")
    }

    @Test
    fun `Testing number eval function`(){
        val result = number.eval()

        assertThat(result).isEqualTo(40.0)
    }
}