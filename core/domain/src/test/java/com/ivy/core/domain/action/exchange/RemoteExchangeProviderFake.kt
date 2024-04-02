package com.ivy.core.domain.action.exchange

import com.ivy.data.CurrencyCode
import com.ivy.data.ExchangeRatesMap
import com.ivy.data.exchange.ExchangeProvider
import com.ivy.exchange.RemoteExchangeProvider



class RemoteExchangeProviderFake: RemoteExchangeProvider { // Remote Exchange Fake to act as the api exchange provider to simulate real data.

    var ratesMap = mapOf(
        "USD" to mapOf(
            "EUR" to 0.92, // this means 1 USD would correspond to 0.91 euros.
            "GHC" to 13.3,
            "AUD" to 1.53,
            "GBP" to -0.79
        ),
        "EUR" to mapOf(
            "GHC" to 14.28,
            "AUD" to 1.66,
            "GBP" to 0.86,
            "ZAR" to 20.45,
        )
    )

    override suspend fun fetchExchangeRates(baseCurrency: CurrencyCode): RemoteExchangeProvider.Result {
        return RemoteExchangeProvider.Result(
            ratesMap = ratesMap[baseCurrency] as ExchangeRatesMap,
            provider = ExchangeProvider.Fawazahmed0
        )

    }
}