package com.ivy.core.domain.action.account

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ivy.core.domain.action.transaction.TimeProviderFake
import com.ivy.core.persistence.dao.account.AccountDao
import com.ivy.core.persistence.entity.account.AccountEntity
import com.ivy.data.Sync
import com.ivy.data.SyncState
import com.ivy.data.account.Account
import com.ivy.data.account.AccountState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

class AccountsActTest {

    private lateinit var accountsAct: AccountsAct
    private lateinit var accountDao: AccountDao
    private lateinit var timeProviderFake: TimeProviderFake


    @BeforeEach
    fun setUp() {
        //Intializing Variables with mocked AccountDao, Time ProviderFake instance and AccountsAct
        accountDao = mockk()
        timeProviderFake = TimeProviderFake()
        accountsAct = AccountsAct(accountDao, timeProviderFake)
    }

    @Test
    fun `action should return transformed accounts`() = runBlocking {
        // List of AccountEntity objects to be returned by the mocked DAO
        val accountEntities = listOf(
            AccountEntity(
                id = UUID.randomUUID().toString(),
                name = "Test account",
                currency = "EUR",
                color = 0x00f15e,
                icon = null,
                excluded = false,
                folderId = null,
                orderNum = 1.0,
                state = AccountState.Default,
                sync =  SyncState.Syncing,
                lastUpdated = Instant.now()
            ),
            AccountEntity(
                id = UUID.randomUUID().toString(),
                name = "Test account 2",
                currency = "USD",
                color = 0x00f15e,
                icon = null,
                excluded = false,
                folderId = null,
                orderNum = 1.0,
                state = AccountState.Default,
                sync =  SyncState.Syncing,
                lastUpdated = Instant.now()
            )
        )

        //The expected list of Account objects after they have been mapped using the toDomain function
        val expectedAccounts = accountEntities.map { toDomain(it, timeProviderFake) }

        //Mock the findAllOrdered method to return the list of account entities
        coEvery { accountDao.findAllOrdered() } returns accountEntities

        //Call the action method to verify the result
        val result = accountsAct(Unit)

        assertThat(expectedAccounts).isEqualTo(result)
    }

}