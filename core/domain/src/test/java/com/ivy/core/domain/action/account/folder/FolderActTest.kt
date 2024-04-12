package com.ivy.core.domain.action.account.folder

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ivy.common.androidtest.TimeProviderFake
import com.ivy.common.androidtest.test_data.accountEntityOrderNum
import com.ivy.common.androidtest.test_data.accountFolderEntity
import com.ivy.core.domain.action.account.AccountFolderDaoFake
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FolderActTest{
    private lateinit var accountFolderDaoFake: AccountFolderDaoFake
    private lateinit var timeProviderFake: TimeProviderFake
    private lateinit var folderAct: FolderAct

    @BeforeEach
    fun setUp(){
        accountFolderDaoFake = AccountFolderDaoFake()
        timeProviderFake = TimeProviderFake()
        folderAct = FolderAct(accountFolderDaoFake,timeProviderFake)
    }

    @Test
    fun `testing folder action`()= runBlocking{
        var flder1 = accountFolderEntity(ordernum = 2.0)
        flder1 = flder1.copy(name = "CheckingFolder")
        var flder2 = accountFolderEntity(ordernum = 4.0)
        flder2 = flder2.copy(name = "SavingFolder")

            accountFolderDaoFake.save(listOf(
                flder1,
                flder2
            ))

        val result =  folderAct.invoke(flder1.id)


        assertThat(result?.name).isEqualTo(flder1.name)
        assertThat(result?.orderNum).isEqualTo(flder1.orderNum)


    }

}