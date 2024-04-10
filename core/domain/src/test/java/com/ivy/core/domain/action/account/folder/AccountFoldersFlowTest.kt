package com.ivy.core.domain.action.account.folder

import assertk.assertThat
import com.ivy.common.androidtest.TimeProviderFake
import com.ivy.common.androidtest.test_data.accountFolderEntity2
import com.ivy.common.androidtest.test_data.accountSample
import com.ivy.core.domain.action.account.AccountFolderDaoFake
import com.ivy.core.domain.action.account.AccountsFlow
import com.ivy.core.domain.action.data.AccountListItem
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AccountFoldersFlowTest{
    private lateinit var accountFoldersFlow: AccountFoldersFlow
    private lateinit var accountsFlow: AccountsFlow
    private lateinit var accountFolderDao: AccountFolderDaoFake
    private lateinit var timeProviderFake: TimeProviderFake

    @BeforeEach
    fun setUp(){
        accountsFlow = mockk<AccountsFlow>()
        accountFolderDao = AccountFolderDaoFake()
        timeProviderFake = TimeProviderFake()

        val acc1 = accountSample(name = "Savings", currency = "USD")
        val acc2 = accountSample(name = "Checkings", currency= "USD")
        val accounts = listOf(
            acc1,
            acc2
        )

        // Mock the behavior of accountsFlow to return a predefined list of accounts
        coEvery { accountsFlow.invoke() } returns flowOf(
            listOf(
                acc1,
                acc2
            )
        )
        // Populate the AccountFolderDaoFake with some folders
        runBlocking {
            accountFolderDao.save(
                listOf(
                    accountFolderEntity2(1.0,"folder1",),
                    accountFolderEntity2(2.0,"folder2",),
                )
            )
        }
        // Initialize AccountFoldersFlow with the mocked and fake dependencies
        accountFoldersFlow = AccountFoldersFlow(accountsFlow, accountFolderDao, timeProviderFake)
    }


    @Test
    fun `AccountFoldersFlow should combine accounts and folders correctly`() = runBlocking {
        val acc1 = accountSample(name = "Savings", currency = "USD")
        val acc2 = accountSample(name = "Checkings", currency= "USD")
        val accounts = listOf(
            acc1,
            acc2
        )
        // Execute the flow and collect the result
        val result = accountFoldersFlow.invoke(Unit).first()

        // Assertions to verify the combined output is as expected
        assertEquals(4, result.size) // Should contain two folders and one archived account item
        //assert(result.any { it is AccountListItem.Archived }) // Verify archived accounts are wrapped in an Archived item
    }


}