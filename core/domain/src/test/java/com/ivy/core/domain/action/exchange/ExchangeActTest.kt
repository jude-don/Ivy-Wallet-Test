package com.ivy.core.domain.action.exchange

import com.ivy.data.CurrencyCode
import com.ivy.data.Value
import com.ivy.data.exchange.ExchangeRates

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ExchangeActTest {

    private lateinit var exchangeAct: ExchangeAct
    private val exchangeRatesFlow = mockk<ExchangeRatesFlow>()

    @BeforeEach
    fun setUp() {
        // Mock the exchange rates flow to provide predetermined responses
        val exchangeRates = ExchangeRates(
            baseCurrency = "USD",
            rates = mapOf("EUR" to 0.92, "GHC" to 5.8)
        )

        every { exchangeRatesFlow() } returns flow { emit(exchangeRates) }

        exchangeAct = ExchangeAct(exchangeRatesFlow)
    }

    @Test
    fun `exchange successfully converts to the target currency`() = runBlocking {
        // Given
        val inputAmount = Value(100.0, "USD")
        val targetCurrency = "EUR"

        // When
        val result = exchangeAct.action(ExchangeAct.Input(inputAmount, targetCurrency))

        // Then
        val expectedAmount = Value(92.0, targetCurrency)
        assertEquals(expectedAmount, result)
    }
}
