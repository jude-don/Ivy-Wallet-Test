package com.ivy.core.domain.action.account

import com.ivy.core.persistence.dao.account.AccountFolderDao
import com.ivy.core.persistence.entity.account.AccountFolderEntity
import com.ivy.data.SyncState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AccountFolderDaoFake : AccountFolderDao {
    private val folders = mutableListOf<AccountFolderEntity>()

    override suspend fun save(values: List<AccountFolderEntity>) {
        // This simple implementation clears and replaces all existing folders.
        // You might want to merge or update the list in a more complex scenario.
        folders.clear()
        folders.addAll(values)
    }

    override suspend fun findAllBlocking(): List<AccountFolderEntity> {
        // Return a copy of the list to simulate fetching from a database.
        return folders.toList()
    }

    override fun findAll(): Flow<List<AccountFolderEntity>> {
        // Returns a flow that emits the current list of folders. This is a simple implementation and won't emit new values if folders change.
        return flowOf(folders.toList())
    }

    override suspend fun findById(folderId: String): AccountFolderEntity? {
        // Finds a folder by ID or returns null if not found.
        return folders.find { it.id == folderId }
    }

    override suspend fun findMaxOrderNum(): Double? {
        // Returns the highest orderNum among all folders or null if no folders are present.
        return folders.maxByOrNull { it.orderNum }?.orderNum
    }

    override suspend fun updateSync(folderId: String, sync: SyncState) {
        // Find the folder by ID and update its sync state.
        folders.find { it.id == folderId }?.let {
            it.sync = sync
        }
    }
}
