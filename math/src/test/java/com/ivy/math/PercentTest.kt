package com.ivy.math

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PercentTest{
    private lateinit var percent: Percent

    @BeforeEach
    fun setUp(){
        percent = Percent(
            TreeNodeFake(40.0)
        )
    }

    @Test
    fun `Test percent print function`(){
        val result = percent.print()

        assertThat(result).isEqualTo("(40.0)%")
    }

    @Test
    fun `Test percent eval function`(){
        val result =  percent.eval()

        assertThat(result).isEqualTo(0.4)
    }
}