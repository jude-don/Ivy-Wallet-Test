package com.ivy.common.androidtest.test_data

import com.ivy.common.androidtest.TimeProviderFake
import com.ivy.core.persistence.dao.trn.SaveTrnData
import com.ivy.core.persistence.entity.trn.TransactionEntity
import com.ivy.core.persistence.entity.trn.data.TrnTimeType
import com.ivy.data.Sync
import com.ivy.data.SyncState
import com.ivy.data.Value
import com.ivy.data.transaction.Transaction
import com.ivy.data.transaction.TransactionType
import com.ivy.data.transaction.TrnMetadata
import com.ivy.data.transaction.TrnPurpose
import com.ivy.data.transaction.TrnState
import com.ivy.data.transaction.TrnTime
import java.time.Instant
import java.util.UUID


fun transactionFun(): Transaction{
    val account = accountSample("Test account","EUR")
    val timeProviderFake = TimeProviderFake()
    return Transaction(
        id = UUID.randomUUID(),
        account = account,
        category = null,
        type = TransactionType.Expense,
        value = Value(50.0,account.currency),
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
}


fun transactionEntity(): TransactionEntity{
    return TransactionEntity(
        id = UUID.randomUUID().toString(),
        accountId = "test-account",
        type = TransactionType.Expense,
        amount = 50.0,
        currency = "EUR",
        time =  Instant.now(),
        timeType = TrnTimeType.Due,
        title = "Test transaction",
        description = "Test description",
        categoryId = null,
        state = TrnState.Default,
        purpose = TrnPurpose.Fee,
        sync = SyncState.Syncing,
        lastUpdated = Instant.now()
    )
}

fun transactionWithTime(
    time: Instant,
    transaction: TransactionEntity = transactionEntity()
): TransactionEntity{
    return transaction.copy(
        time = time,
        lastUpdated = time
    )
}


fun saveTrnData(entity: TransactionEntity = transactionEntity() ): SaveTrnData {
    return SaveTrnData(
        entity = entity,
        tags = emptyList(),
        attachments = emptyList(),
        metadata = emptyList()


    )
}



