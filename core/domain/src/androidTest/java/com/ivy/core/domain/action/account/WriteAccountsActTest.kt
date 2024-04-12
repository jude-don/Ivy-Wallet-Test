package com.ivy.core.domain.action.account

import assertk.Assert
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.ivy.common.androidtest.IvyAndroidTest
import com.ivy.common.androidtest.test_data.accountSample
import com.ivy.core.domain.action.data.Modify
import com.ivy.core.persistence.dao.account.AccountDao
import com.ivy.core.persistence.entity.account.AccountEntity
import com.ivy.data.Sync
import com.ivy.data.SyncState
import com.ivy.data.account.Account
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject


@HiltAndroidTest
class WriteAccountsActTest: IvyAndroidTest(){

    @Inject
    lateinit var writeAccountsAct: WriteAccountsAct // This is to inject the actual dependency

    @Inject
    lateinit var accountDao: AccountDao

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSaveUpdateAccount() = runTest{
        val syncTime =  LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.SECONDS)
        val acc1 = accountSample(name = "Savings", currency = "USD").copy(sync = Sync(SyncState.Syncing,syncTime))

        writeAccountsAct(Modify.save(acc1))

        val createdAccountFromDb = accountDao.findAllBlocking().first()

        assertk.assertThat(createdAccountFromDb)
            .transformToAccount()
            .isEqualTo(acc1)

        val updatedAccount =  acc1.copy(
            name = "updated",
            currency = "CAD"
        )
        writeAccountsAct(Modify.save(updatedAccount))

        val accountsFromDb = accountDao.findAllBlocking()
        assertk.assertThat(accountsFromDb).hasSize(1)

        val updatedAccountFromDb = accountsFromDb.first()

        assertk.assertThat(updatedAccountFromDb)
            .transformToAccount()
            .isEqualTo(updatedAccount)

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testingDeleteAccount() = runTest {
        val syncTime =  LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.SECONDS)
        val acc1 = accountSample(name = "Savings", currency = "USD").copy(sync = Sync(SyncState.Syncing,syncTime))
        writeAccountsAct(Modify.save(acc1))
        writeAccountsAct(Modify.delete(acc1.id.toString()))
        val createdAccounts = accountDao.findAllBlocking()

        assertTrue(createdAccounts.isEmpty())
    }

    private fun Assert<AccountEntity>.transformToAccount(): Assert<Account>{ //This is to map the AccountEntity to the Account
        return transform{
            Account(
                id = UUID.fromString(it.id),
                name = it.name,
                currency = it.currency,
                color = it.color,
                icon = it.icon,
                excluded = it.excluded,
                folderId = it.folderId?.let{ id->
                    UUID.fromString(id)
                },
                orderNum = it.orderNum,
                state = it.state,
                sync = Sync(
                    state = it.sync,
                    lastUpdated = LocalDateTime.ofInstant(it.lastUpdated, ZoneId.systemDefault())
                )

            )
        }
    }
}