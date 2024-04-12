package com.ivy.core.domain.action.account.folder

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ivy.common.androidtest.test_data.accountSample
import com.ivy.common.toUUID
import com.ivy.core.domain.action.account.AccountsFlow
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AccountsInFolderActTest{
    private lateinit var accountsInFolderAct: AccountsInFolderAct
    private lateinit var accountsFlow: AccountsFlow


    @BeforeEach
    fun setUp(){
        accountsFlow = mockk()

        accountsInFolderAct = AccountsInFolderAct(accountsFlow)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `action returns list of accounts in specified folder`() = runTest {
        var acc1 = accountSample(name="account1", currency = "EUR")
        var acc2 =  accountSample(name="account2", currency="USD")
        val id1 = "folder-uuid"
        val id2 = "next-folder-uuid"
        val accId1 = id1.toUUID()
        val accId2 = id2.toUUID()
        acc1 = acc1.copy(id = accId1)
        acc2 = acc2.copy(id = accId2)

        val mockAccounts = listOf(
            acc1,
            acc2
        )

        // Mock the behavior of accountsFlow to emit mockAccounts
        coEvery { accountsFlow.invoke() } returns flowOf(mockAccounts)

        // Execute the action
        val result =  accountsInFolderAct.invoke(id1)

        // Assert that the result only contains the account in the specified folder
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0]).isEqualTo(acc1)

    }
}