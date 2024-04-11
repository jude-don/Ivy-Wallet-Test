package com.ivy.core.domain.action.calculate.account

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ivy.common.androidtest.test_data.accountSample
import com.ivy.core.domain.action.calculate.Stats
import com.ivy.data.Value
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AccBalanceFlowTest{
    private lateinit var accBalanceFlow: AccBalanceFlow
    private lateinit var accStatsFlow: AccStatsFlow


    @BeforeEach
    fun setUp(){
        // Mock AccStatsFlow
        accStatsFlow = mockk()

        //Intialize AccBalanceFlow with the mocked AccStatsFlow
        accBalanceFlow = AccBalanceFlow(accStatsFlow)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `createFlow should map AccStatsFlow output to Value`() = runTest{
        val account =  accountSample("Test Account", "USD")
        val inputCurrency = "USD"
        val expectedBalance = Value(amount = 100.0, currency = inputCurrency)
        val income = Value(amount = 200.0, currency = inputCurrency)
        val expense = Value(amount = 100.0, currency = inputCurrency)
        val input = AccBalanceFlow.Input(account,inputCurrency)
        val mockStats = Stats(balance = expectedBalance, income = income, expense = expense, incomesCount = 1, expensesCount = 1)

        // Mock the behavior of accStatsFlow to emit mockStats when invoked with specific input
        coEvery { accStatsFlow(any<AccStatsFlow.Input>()) } returns flowOf(mockStats)

        // Execute the createFlow function
        val result = accBalanceFlow(input).first()

        // Assert that the result is the expected balance
        assertThat(result.amount).isEqualTo(expectedBalance.amount)
        //assertEquals(expectedBalance, result.amount)

    }
}