package com.ivy.core.domain.action.exchange

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ivy.data.Value
import com.ivy.data.exchange.ExchangeRates
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SumValuesInCurrencyFlowTest{
    private lateinit var sumValuesInCurrencyFlow: SumValuesInCurrencyFlow
    private lateinit var exchangeRatesFlow: ExchangeRatesFlow

    @BeforeEach
    fun setUp() {
        exchangeRatesFlow = mockk()
        sumValuesInCurrencyFlow = SumValuesInCurrencyFlow(exchangeRatesFlow)

        // Setup the mock for exchangeRatesFlow to return specific exchange rates
        coEvery { exchangeRatesFlow() } returns flowOf(ExchangeRates(baseCurrency = "USD", rates = mapOf("EUR" to 2.0, "USD" to 4.0, "CAD" to 0.8)))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `SumValuesInCurrencyFlow should correctly sum values in the desired currency`() = runBlocking<Unit> {
        val inputValues = listOf(
            Value(100.0, "EUR"), // 100 EUR
            Value(50.0, "USD")   // 50 USD
        )

        val outputCurrency = "USD"

        sumValuesInCurrencyFlow(
            SumValuesInCurrencyFlow.Input(
                values = inputValues,
                outputCurrency = outputCurrency
            )
        ).test {
            val result = awaitItem()
            assertThat(result.amount).isEqualTo(100.0) // 100 EUR * 1.2 (rate) + 50 USD
            assertThat(result.currency).isEqualTo("USD")
            cancelAndIgnoreRemainingEvents()
        }
    }
}