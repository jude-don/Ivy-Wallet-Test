package com.ivy.core.domain.action.account

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ivy.common.androidtest.test_data.accountEntityOrderNum
import com.ivy.common.androidtest.test_data.accountFolderEntity
import com.ivy.core.persistence.entity.account.AccountEntity
import com.ivy.core.persistence.entity.account.AccountFolderEntity
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NewAccountTabItemOrderNumActTest {
    private lateinit var newAccountTabItemOrderNumAct: NewAccountTabItemOrderNumAct
    private lateinit var accountDaoFake: AccountDaoFake
    private lateinit var folderDaoFake: AccountFolderDaoFake

    @BeforeEach
    fun setUp() {
        // Initialize the fakes
        accountDaoFake = AccountDaoFake()
        folderDaoFake = AccountFolderDaoFake()

        // Populate the fakes with some test data
        runBlocking {
            accountDaoFake.save(listOf(
                accountEntityOrderNum(ordernum = 1.0) ,
                accountEntityOrderNum(ordernum = 3.0)
            ))

            folderDaoFake.save(listOf(
                accountFolderEntity(ordernum = 2.0),
                accountFolderEntity(ordernum = 4.0),
            ))
        }

        // Initialize the action class with the fakes
        newAccountTabItemOrderNumAct = NewAccountTabItemOrderNumAct(accountDaoFake, folderDaoFake)
    }

    @Test
    fun `action should return one plus the maximum orderNum from accounts and folders`() = runBlocking {
        // Invoke the action
        val result = newAccountTabItemOrderNumAct(Unit)

        // Assert that the result is one plus the maximum orderNum, which is 4.0 from folders, so expected is 5.0
        assertThat(result).isEqualTo(5.0)
    }
}