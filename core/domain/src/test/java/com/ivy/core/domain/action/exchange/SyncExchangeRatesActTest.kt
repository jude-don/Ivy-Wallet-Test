package com.ivy.core.domain.action.exchange

import assertk.assertThat
import assertk.assertions.isNotNull
import com.ivy.core.data.calculation.ExchangeRates
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SyncExchangeRatesActTest { // This is to test the exchange rates class

    private lateinit var syncExchangeRatesAct: SyncExchangeRatesAct
    private lateinit var exchangeProviderFake: RemoteExchangeProviderFake
    private lateinit var exchangeRateDaoFake: ExchangeRateDaoFake


    @BeforeEach
    fun setUp() {
        exchangeProviderFake = RemoteExchangeProviderFake()
        exchangeRateDaoFake = ExchangeRateDaoFake()
        syncExchangeRatesAct = SyncExchangeRatesAct(
            exchangeProvider = exchangeProviderFake,
            exchangeRateDao = exchangeRateDaoFake
        )
    }


    @Test
    fun `Test sync exchange rates, negative values ignored`() =
        runBlocking<Unit> { // Since syncExchangeRatesAct is a suspend function we need to put it in a runBlocking since coroutines are not available here
            syncExchangeRatesAct("USD")

            val usdRates = exchangeRateDaoFake
                .findAllByBaseCurrency("USD")
                .first {
                    it.isNotEmpty()
                }
            val gbpRate =
                usdRates.find { it.currency == "GBP" } // To find the rate with a negative value

            assertNull(gbpRate)
        }


    @Test
    fun `Test sync exchange rates, valid values are saved`() = runBlocking<Unit> {
        syncExchangeRatesAct("EUR")

        val eurRates = exchangeRateDaoFake
            .findAllByBaseCurrency("EUR")
            .first{
                it.isNotEmpty()
            }
        val ghcRate =  eurRates.find { it.currency == "GHC" }
        val zarRate = eurRates.find { it.currency == "ZAR" }

        assertThat(ghcRate).isNotNull()
        assertThat(zarRate).isNotNull()
    }

}