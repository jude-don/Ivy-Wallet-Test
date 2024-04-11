package com.ivy.core.domain.action.account

import com.ivy.common.androidtest.TimeProviderFake
import com.ivy.common.androidtest.test_data.accountSample
import com.ivy.common.time.provider.TimeProvider
import com.ivy.core.domain.action.data.Modify
import com.ivy.core.domain.action.transaction.WriteTrnsAct
import com.ivy.core.domain.algorithm.accountcache.InvalidateAccCacheAct
import com.ivy.core.persistence.dao.account.AccountDao
import com.ivy.core.persistence.query.TrnQueryExecutor
import com.ivy.data.SyncState
import com.ivy.data.account.Account
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WriteAccountsActTest{
    private lateinit var writeAccountsAct: WriteAccountsAct
    private lateinit var accountDaoFake: AccountDaoFake
    private lateinit var writeTrnsAct: WriteTrnsAct
    private lateinit var trnQueryExecutor: TrnQueryExecutor
    private lateinit var timeProvider: TimeProvider
    private lateinit var invalidateAccCacheAct: InvalidateAccCacheAct



    @BeforeEach
    fun setUp(){

        // Initialize the parameters
        timeProvider = TimeProviderFake()
        trnQueryExecutor = mockk<TrnQueryExecutor>()
        invalidateAccCacheAct = mockk<InvalidateAccCacheAct>(relaxed = true)
        writeTrnsAct = mockk<WriteTrnsAct>(relaxed = true)
        accountDaoFake = AccountDaoFake()

        // Mock the TrnQueryExecutor to return an empty list of transactions
        coEvery { trnQueryExecutor.query(any()) } returns emptyList()

        // Initialize the WriteAccountsAct with the fakes and mocks
        writeAccountsAct = WriteAccountsAct(accountDaoFake,writeTrnsAct,trnQueryExecutor,timeProvider,invalidateAccCacheAct)
    }

    @Test
    fun `saving new accounts should call save on accountDao with correct entities`() = runBlocking {
        val acc1 = accountSample(name = "Savings", currency = "USD")
        val acc2 = accountSample(name = "Checkings", currency= "USD")

        val accountLists = listOf(
            acc1,
            acc2
        )

        // When saving accounts
        writeAccountsAct(Modify.Save(accountLists))


        //assertions
        assert(accountDaoFake.accountsReadOnly.any { it.name == "Savings" && it.currency == "USD" })
        assert(accountDaoFake.accountsReadOnly.any { it.name == "Checkings" && it.currency == "USD" })
    }

    @Test
    fun `deleting an account should call delete on accountDao with correct ID and update transactions`() = runBlocking {

        val acc1 = accountSample(name = "Savings", currency = "USD")
        val acc2 = accountSample(name = "Checkings", currency= "USD")
        val accountLists = listOf(
            acc1,
            acc2
        )

        // When saving accounts
        writeAccountsAct(Modify.Save(accountLists))

        // When deleting an account
        writeAccountsAct(Modify.Delete(listOf(acc1.id.toString())))

        // Then accountDao's updateSync should be called with Deleting state and WriteTrnsAct should handle transaction deletions
        coVerify {
            accountDaoFake.updateSync(acc1.id.toString(), SyncState.Deleting)
            writeTrnsAct(any<WriteTrnsAct.Input.ManyInefficient>())
        }
    }



}