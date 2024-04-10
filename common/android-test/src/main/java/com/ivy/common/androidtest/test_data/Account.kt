package com.ivy.common.androidtest.test_data

import com.ivy.core.persistence.IvyWalletCoreDb
import com.ivy.core.persistence.entity.account.AccountEntity
import com.ivy.core.persistence.entity.account.AccountFolderEntity
import com.ivy.core.persistence.entity.trn.TransactionEntity
import com.ivy.data.Sync
import com.ivy.data.SyncState
import com.ivy.data.account.Account
import com.ivy.data.account.AccountState
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

fun accountSample(name: String, currency:String): Account{
    return Account(
        id = UUID.randomUUID(),
        name = name,
        currency = currency,
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
}


fun accountEntity(): AccountEntity{
    return AccountEntity(
        id = UUID.randomUUID().toString(),
        name = "Test account",
        currency = "EUR",
        color = 0x000000,
        icon = null,
        folderId = null,
        orderNum = 1.0,
        excluded = false,
        state = AccountState.Default,
        sync = SyncState.Syncing,
        lastUpdated = Instant.now()
    )
}

fun accountEntityOrderNum(ordernum:Double): AccountEntity{
    return AccountEntity(
        id = UUID.randomUUID().toString(),
        name = "Test account",
        currency = "EUR",
        color = 0x000000,
        icon = null,
        folderId = null,
        orderNum = ordernum,
        excluded = false,
        state = AccountState.Default,
        sync = SyncState.Syncing,
        lastUpdated = Instant.now()
    )
}

fun accountFolderEntity(ordernum: Double): AccountFolderEntity{
    return AccountFolderEntity(
        id = UUID.randomUUID().toString(),
        name = "Test account",
        color = 0x000000,
        icon = null,
        orderNum = ordernum,
        sync = SyncState.Syncing,
        lastUpdated = Instant.now()
    )
}


fun accountFolderEntity2(ordernum: Double, name: String): AccountFolderEntity{
    return AccountFolderEntity(
        id = UUID.randomUUID().toString(),
        name = name,
        color = 0x000000,
        icon = null,
        orderNum = ordernum,
        sync = SyncState.Syncing,
        lastUpdated = Instant.now()
    )
}

suspend fun IvyWalletCoreDb.saveAccountWithTransactions(
    accountEntity: AccountEntity = accountEntity(),
    transactions:List<TransactionEntity> = listOf(transactionEntity())
){
    accountDao().save(listOf(accountEntity))

    val transactionsWithAccount = transactions.map{
        it.copy(
            accountId = accountEntity.id
        )
    }
    transactionsWithAccount.forEach {
        trnDao().save(saveTrnData(it))
    }
}