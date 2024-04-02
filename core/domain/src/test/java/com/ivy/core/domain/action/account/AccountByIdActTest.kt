package com.ivy.core.domain.action.account

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ivy.core.domain.action.transaction.TimeProviderFake
import com.ivy.core.persistence.dao.account.AccountDao
import com.ivy.core.persistence.entity.account.AccountEntity
import com.ivy.data.SyncState
import com.ivy.data.account.AccountState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.UUID

class AccountByIdActTest{
    private lateinit var accountByIdAct: AccountByIdAct
    private lateinit var accountDao: AccountDao
    private lateinit var timeProviderFake: TimeProviderFake


    @BeforeEach
    fun setUp(){
        accountDao = mockk()
        timeProviderFake = TimeProviderFake()
        accountByIdAct = AccountByIdAct(accountDao,timeProviderFake)
    }


    @Test
    fun `action should return account when the id is searched`(): Unit = runBlocking {
        val account = AccountEntity(
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
        )

        val expectedAccount = toDomain(account,timeProviderFake)

        coEvery { accountDao.findById(account.id) } returns account

        val result = accountByIdAct(account.id)

        assertThat(expectedAccount).isEqualTo(result)
    }
}