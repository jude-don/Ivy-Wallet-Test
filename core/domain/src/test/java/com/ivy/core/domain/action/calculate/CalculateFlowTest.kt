package com.ivy.core.domain.action.calculate


import com.ivy.common.androidtest.test_data.transactionFun
import com.ivy.core.domain.action.exchange.ExchangeRatesFlow
import com.ivy.data.Sync
import com.ivy.data.SyncState
import com.ivy.data.Value
import org.junit.jupiter.api.Assertions.*
import com.ivy.data.exchange.ExchangeRates
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.ivy.data.transaction.Transaction
import com.ivy.data.transaction.TransactionType
import com.ivy.data.transaction.TrnMetadata
import com.ivy.data.transaction.TrnPurpose
import com.ivy.data.transaction.TrnState
import com.ivy.data.transaction.TrnTime
import kotlinx.coroutines.flow.toList
import java.util.UUID

class CalculateFlowTest{

    private lateinit var calculateFlow: CalculateFlow
    private lateinit var exchangeRatesFlow: ExchangeRatesFlow
    private lateinit var rates: ExchangeRates

    @BeforeEach
    fun setUp() {
        exchangeRatesFlow = mockk()
        calculateFlow = CalculateFlow(exchangeRatesFlow)

        rates = ExchangeRates(
            baseCurrency = "USD",
            rates = mapOf("EUR" to 1.5, "GHS" to 2.0)
        )

        coEvery { exchangeRatesFlow() } returns flowOf(rates)
    }

    @Test
    fun `should calculate correct stats when including transfers and hidden transactions`() = runBlocking {
        val transactions = listOf(
            transactionFun().copy(value = Value(100.0, "GHS"), type = TransactionType.Income),
            transactionFun().copy(value = Value(50.0, "EUR"), type = TransactionType.Expense),
        )

        val input = CalculateFlow.Input(
            trns = transactions,
            includeTransfers = true,
            includeHidden = true,
            outputCurrency = "GHS"
        )

        val stats = calculateFlow(input).toList().first()

        assertEquals(100.0, stats.income.amount) // 100 USD + 60 USD + 50 EUR converted to USD
        assertEquals(66.66666666666666, stats.expense.amount) // 30 EUR converted to USD + 20 USD
        assertEquals(1, stats.incomesCount)
        assertEquals(1, stats.expensesCount)
    }

    @Test
    fun `should calculate correct stats excluding transfers and hidden transactions`() = runBlocking{
        val transactions = listOf(
            transactionFun().copy(value = Value(100.0, "GHS"), type = TransactionType.Income),
            transactionFun().copy(value = Value(50.0, "EUR"), type = TransactionType.Expense),
            transactionFun().copy(value = Value(20.0, "GHS"), type = TransactionType.Income),
            transactionFun().copy(value = Value(30.0, "EUR"), type = TransactionType.Expense),
            transactionFun().copy(value = Value(60.0, "GHS"), type = TransactionType.Income),
        )

        val input = CalculateFlow.Input(
            trns = transactions,
            includeTransfers = false,
            includeHidden = false,
            outputCurrency = "EUR"
        )

        val stats = calculateFlow(input).toList().first()

        assertEquals(135.0, stats.income.amount) // Only 100 USD as hidden income is not included
        assertEquals(80.0, stats.expense.amount) // 50 EUR converted to USD
        assertEquals(3, stats.incomesCount)
        assertEquals(2, stats.expensesCount)
    }
}