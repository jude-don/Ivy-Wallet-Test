package com.ivy.core.domain.action.account

import com.ivy.core.data.Transaction
import com.ivy.core.domain.action.calculate.account.AccBalanceFlow
import com.ivy.core.domain.action.transaction.TimeProviderFake
import com.ivy.core.domain.action.transaction.WriteTrnsAct
import com.ivy.core.domain.pure.account.adjustBalanceTrn
import com.ivy.core.persistence.entity.account.AccountEntity
import com.ivy.data.Sync
import com.ivy.data.SyncState
import com.ivy.data.Value
import com.ivy.data.account.Account
import com.ivy.data.account.AccountState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import com.ivy.data.transaction.*
import java.time.LocalDateTime
import java.util.UUID

class AdjustAccBalanceActTest {
    private lateinit var adjustAccBalanceAct: AdjustAccBalanceAct
    private lateinit var writeTrnsAct: WriteTrnsAct
    private lateinit var accBalanceFlow: AccBalanceFlow
    private lateinit var timeProviderFake: TimeProviderFake


    @BeforeEach
    fun setUp() {
        writeTrnsAct = mockk(relaxed = true)
        accBalanceFlow = mockk()
        timeProviderFake = TimeProviderFake()
        adjustAccBalanceAct = AdjustAccBalanceAct(writeTrnsAct, accBalanceFlow, timeProviderFake)
    }

    @Test
    fun `Test to check if the account balance is adjusted and a transaction is created`() =
        runBlocking {
            val account = Account(
                id = UUID.randomUUID(),
                name = "Test account",
                currency = "EUR",
                color = 0x00f15e,
                icon = null,
                excluded = false,
                folderId = null,
                orderNum = 1.0,
                state = AccountState.Default,
                sync = Sync(
                    state = SyncState.Syncing,
                    lastUpdated = LocalDateTime.now()
                )
            )
            val desiredBalance = 150.0
            val hideTransaction = false
            val currentBalanceValue = Value(50.0,account.currency)


            //Mock the AccBalanceFlow to return the current balance
            coEvery { accBalanceFlow.invoke(any()) } returns flowOf(currentBalanceValue)

            //Call the adjustBalanceTrn function
            val adjustmentTransaction1 = adjustBalanceTrn(
                timeProviderFake,
                account,
                currentBalanceValue.amount,
                desiredBalance,
                hideTransaction
            )

            val adjustmentTransaction = Transaction(
                id = UUID.randomUUID(),
                account = account,
                category = null,
                type = TransactionType.Expense,
                value = currentBalanceValue,
                title = "Adjust balance",
                description = null,
                time = TrnTime.Actual(timeProviderFake.timeNow()),
                state = TrnState.Default,
                purpose = TrnPurpose.AdjustBalance,
                attachments = emptyList(),
                sync = Sync(
                    state = SyncState.Syncing,
                    lastUpdated = timeProviderFake.timeNow()
                ),
                tags = emptyList(),
                metadata = TrnMetadata(recurringRuleId = null, loanId = null, loanRecordId = null)
            )

            //When
            adjustAccBalanceAct(AdjustAccBalanceAct.Input(account,desiredBalance,hideTransaction))
            //adjustAccBalanceAct.invoke(myInput)

            coVerify(exactly = 1) {
                writeTrnsAct(any<WriteTrnsAct.Input.CreateNew>())
            }

        }

}