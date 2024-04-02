package com.ivy.core.domain.action.transaction.transfer

import com.ivy.core.domain.action.transaction.WriteTrnsAct
import com.ivy.core.domain.action.transaction.WriteTrnsBatchAct
import com.ivy.data.Sync
import com.ivy.data.SyncState
import com.ivy.data.Value
import com.ivy.data.account.Account
import com.ivy.data.account.AccountState
import com.ivy.data.transaction.TransactionType
import com.ivy.data.transaction.Transfer
import com.ivy.data.transaction.TrnTime
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

class WriteTransferActTest{

    private lateinit var writeTransferAct: WriteTransferAct
    private lateinit var writeTrnsAct: WriteTrnsAct
    private lateinit var writeTrnsBatchAct: WriteTrnsBatchAct
    private lateinit var transferByBatchIdAct: TransferByBatchIdAct




    @BeforeEach
    fun setUp(){
        writeTrnsAct =  mockk(relaxed = true)
        writeTrnsBatchAct = mockk(relaxed = true)
        transferByBatchIdAct = mockk(relaxed = true)
        writeTransferAct = WriteTransferAct(
            writeTrnsAct = writeTrnsAct,
            writeTrnsBatchAct = writeTrnsBatchAct,
            transferByBatchIdAct = transferByBatchIdAct,
        )
    }


    @Test
    fun`Add transfer, fees are considered`() = runBlocking { // This test is used to check if transactionsare added to the list of transactions
        writeTransferAct(
            ModifyTransfer.add(
                data =  TransferData(
                    amountFrom = Value(amount= 50.0, currency = "EUR" ),
                    amountTo = Value(amount= 60.0, currency = "USD"),
                    accountFrom = Account(
                        id = UUID.randomUUID(),
                        name = "Test account1",
                        currency = "EUR" ,
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
                    ),
                    accountTo = Account(
                        id = UUID.randomUUID(),
                        name = "Test account2",
                        currency = "USD" ,
                        color = 0x00f15e,
                        icon = null,
                        excluded = false,
                        folderId = null,
                        orderNum = 1.0,
                        state = AccountState.Archived,
                        sync = Sync(
                            state = SyncState.Syncing,
                            lastUpdated = LocalDateTime.now()
                        )
                    ),
                    category = null,
                    time = TrnTime.Actual(LocalDateTime.now()),
                    title = "Test Transfer",
                    description ="Test transfer decription",
                    fee = Value(amount = 5.0, currency="EUR"),
                    sync = Sync(
                        state = SyncState.Syncing,
                        lastUpdated = LocalDateTime.now()
                    )

                )
            )
        )
        coVerify {// to check if the expected is equal to the actual
            writeTrnsBatchAct(
                match {
                    it as WriteTrnsBatchAct.ModifyBatch.Save

                    val from = it.batch.trns[0]
                    val to =  it.batch.trns[1]
                    val fee = it.batch.trns[2]

                    from.value.amount == 50.0 &&
                            to.value.amount == 60.0 &&
                            fee.value.amount == 5.0 &&
                            fee.type == TransactionType.Expense
                }
            )
        }

    }



//    @Test
//    fun `Delete transfer, removes transactions`() = runBlocking {
//        // Arrange: Prepare a Transfer object with its batch ID and related transactions
//        val fixedTime = LocalDateTime.of(2024, 3, 31, 12, 0) // Use a constant time
//        val fixedUUIDFrom = UUID.fromString("fixed-uuid-string-1") // Use a constant UUID
//        val fixedUUIDTo = UUID.fromString("fixed-uuid-string-2") // Use a constant UUID
//
//
//        val transfer = Transfer(
//            batchId = ,
//            time = ,
//            from = ,
//            to = ,
//            fee = ,
//        )
//
//            // Act: Perform the deleteTransfer action
//            writeTransferAct(ModifyTransfer.Delete(transfer))
//
//        // Assert: Verify that the transactions have been deleted correctly
//        coVerify {
//            writeTrnsBatchAct(match {
//                it is WriteTrnsBatchAct.ModifyBatch.Delete &&
//                        it.batch.batchId == transfer.batchId &&
//                        it.batch.trns.containsAll(listOfNotNull(transfer.from, transfer.to, transfer.fee))
//            })
//        }
//    }
}