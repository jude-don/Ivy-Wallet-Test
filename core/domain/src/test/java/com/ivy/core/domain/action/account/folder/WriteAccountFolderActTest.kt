package com.ivy.core.domain.action.account.folder

import assertk.assertThat
import assertk.assertions.isIn
import com.ivy.common.androidtest.TimeProviderFake
import com.ivy.core.domain.action.account.AccountFolderDaoFake
import com.ivy.core.domain.action.data.Modify
import com.ivy.core.persistence.entity.account.AccountFolderEntity
import com.ivy.data.Sync
import com.ivy.data.SyncState
import com.ivy.data.account.AccountFolder
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class WriteAccountFolderActTest{
    private lateinit var writeAccountFolderAct: WriteAccountFolderAct
    private lateinit var timeProviderFake: TimeProviderFake
    private lateinit var accountFolderDaoFake: AccountFolderDaoFake


    @BeforeEach
    fun setUp(){
        timeProviderFake = TimeProviderFake().apply {
            timeNow = LocalDateTime.of(2024, 1, 1, 12, 0)
            dateNow = LocalDate.of(2024, 1, 1)
            zoneId = ZoneId.of("UTC")
        }
        accountFolderDaoFake = AccountFolderDaoFake()
        writeAccountFolderAct = WriteAccountFolderAct(
            timeProvider = timeProviderFake,
            accountFolderDao = accountFolderDaoFake
        )
    }

    @Test
    fun `Test save with valid data`() = runBlocking {
        val folders = listOf(
            AccountFolder(
                id = "1",
                name = "New Folder",
                icon = null,
                color = 0x00ffff,
                orderNum = 1.0,
                sync = Sync(
                    state = SyncState.Syncing,
                    lastUpdated = LocalDateTime.now()
                )
            )
        )
        writeAccountFolderAct(Modify.Save(folders))

        val savedFolders = accountFolderDaoFake.findAllBlocking()
        assertEquals(1, savedFolders.size)
        assertEquals("New Folder", savedFolders[0].name)
        assertEquals(0x00ffff, savedFolders[0].color)
        assertNull(savedFolders[0].icon)
        assertEquals(SyncState.Syncing, savedFolders[0].sync)
    }

    @Test
    fun `Test save with invalid data`() = runBlocking {
        val folders = listOf(
            AccountFolder(
                id = "1",
                name = "",
                icon = null,
                color = 0x00ffff,
                orderNum = 1.0,
                sync = Sync(
                    state = SyncState.Synced,
                    lastUpdated = LocalDateTime.now()
                )
            )
        )
        writeAccountFolderAct(Modify.Save(folders))

        val savedFolders = accountFolderDaoFake.findAllBlocking()
        assertTrue(savedFolders.isEmpty(), "Invalid folders should not be saved")
    }

    @Test
    fun `Test delete folders`() = runBlocking {
        // Preparing initial data
        val initialFolders = listOf(
            AccountFolderEntity(
                id = "1",
                name = "folder1",
                color = 0x00ffff,
                icon = null,
                orderNum = 1.0,
                sync = SyncState.Synced,
                lastUpdated = Instant.now()
            ),
            AccountFolderEntity(
                id = "2",
                name = "folder2",
                color = 0x00ffff,
                icon = null,
                orderNum = 1.0,
                sync = SyncState.Synced,
                lastUpdated = Instant.now()
            )

        )
        accountFolderDaoFake.save(initialFolders)

        // Perform deletion
        writeAccountFolderAct(Modify.Delete(listOf("1", "2")))

        // Verify updates
        val updatedFolders = accountFolderDaoFake.findAllBlocking()
        assertTrue(updatedFolders.all { it.sync == SyncState.Deleting }, "All folders should be marked as deleting")
    }

}