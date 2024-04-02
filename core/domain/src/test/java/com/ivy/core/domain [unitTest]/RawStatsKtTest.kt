import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ivy.core.domain.algorithm.calc.rawStats
import com.ivy.core.persistence.algorithm.calc.CalcTrn
import com.ivy.data.transaction.TransactionType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

internal class RawStatsKtTest{
    private lateinit var transactions: List<CalcTrn>
    val tenSecondsAgo = Instant.now().minusSeconds(10)
    val fiveSecondsAgo = Instant.now().minusSeconds(5)
    val twoSecondsAgo = Instant.now().minusSeconds(2)
    val sevenSecondsAgo = Instant.now().minusSeconds(7)

    @BeforeEach
    fun setup(){

        transactions = listOf(
            CalcTrn(
                amount = 1000.0, // Sample amount
                currency = "USD", // Sample currency
                type = TransactionType.Income, // Sample transaction type
                time = sevenSecondsAgo // Current time
            ),
            CalcTrn(
                amount = 200.0, // Sample amount
                currency = "USD", // Sample currency
                type = TransactionType.Income, // Sample transaction type
                time = tenSecondsAgo // Current time
            ),
            CalcTrn(
                amount = 50.0, // Sample amount
                currency = "USD", // Sample currency
                type = TransactionType.Expense, // Sample transaction type
                time = fiveSecondsAgo // Current time
            ),
            CalcTrn(
                amount = 100.0, // Sample amount
                currency = "USD", // Sample currency
                type = TransactionType.Expense, // Sample transaction type
                time = twoSecondsAgo // Current time
            )
        )
    }


    @Test
    fun `Evaluating income of rawStats function`(){
        val result = rawStats(transactions)
        assertThat(result.incomes["USD"]).isEqualTo(1200.0)
        assertThat(result.expenses["USD"]).isEqualTo(150.0)
        assertThat(result.incomesCount).isEqualTo(2)
        assertThat(result.expensesCount).isEqualTo(2)
        assertThat(result.newestTrnTime).isEqualTo(twoSecondsAgo)
    }

}