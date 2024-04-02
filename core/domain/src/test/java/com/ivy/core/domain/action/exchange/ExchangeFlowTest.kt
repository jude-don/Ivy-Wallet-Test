package com.ivy.core.domain.action.exchange

import com.ivy.data.Value
import com.ivy.data.exchange.ExchangeRates
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ExchangeFlowTest{
    private val exchangeRatesFlow = mockk<ExchangeRatesFlow>()
    private val exchangeFlow = ExchangeFlow(exchangeRatesFlow)

    @Test
    fun `exchangeFlow should correctly convert currency based on exchange rates`() = runBlocking {
        // Mocking ExchangeRatesFlow to return a specific flow of ExchangeRates
        coEvery { exchangeRatesFlow() } returns flow {
            emit(ExchangeRates(baseCurrency = "USD", rates = mapOf("EUR" to 0.85, "GHS" to 13.0)))
        }

        val input = ExchangeFlow.Input(value = Value(100.0, "USD"), outputCurrency = "EUR")
        val result = exchangeFlow.createFlow(input).first()

        assertEquals(Value(85.0, "EUR"), result)
    }

    @Test
    fun `exchangeFlow should default to base currency if output currency is null`() = runBlocking {
        // Mocking ExchangeRatesFlow to return a specific flow of ExchangeRates
        coEvery { exchangeRatesFlow() } returns flow {
            emit(ExchangeRates(baseCurrency = "USD", rates = mapOf("EUR" to 5.0, "GHS" to 13.0)))
        }

        val input = ExchangeFlow.Input(value = Value(100.0, "EUR"))
        val result = exchangeFlow.createFlow(input).first()

        // Since the output currency is null, it should default to base currency which is USD in this mock
        assertEquals(Value(20.0 /* Approximation */, "USD"), result)
    }

    @Test
    fun `exchangeFlow should handle case where currency conversion is not required`() = runBlocking {
        // Mocking ExchangeRatesFlow to return a specific flow of ExchangeRates
        coEvery { exchangeRatesFlow() } returns flow {
            emit(ExchangeRates(baseCurrency = "USD", rates = mapOf("EUR" to 0.85, "GHS" to 13.0)))
        }

        val input = ExchangeFlow.Input(value = Value(100.0, "USD"), outputCurrency = "USD")
        val result = exchangeFlow.createFlow(input).first()

        // No conversion should take place, the amount remains the same
        assertEquals(Value(100.0, "USD"), result)
    }
}