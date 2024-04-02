package com.ivy.math

import arrow.core.nonEmptyListOf
import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AddTest{
    private lateinit var add: Add

    @BeforeEach
    fun setUp(){
        add = Add(
            nonEmptyListOf(
                TreeNodeFake(1.0),
                TreeNodeFake(3.0),
                TreeNodeFake(4.0)
            )
        )
    }

    @Test
    fun `Testing add print function`(){
       val result = add.print()

        assertThat(result).isEqualTo("(1.0)+(3.0)+(4.0)")
    }

    @Test
    fun `Testing add eval function`(){
        val result = add.eval()

        assertThat(result).isEqualTo(8.0)
    }
}