package com.ivy.core.domain.action.account

import com.ivy.core.persistence.dao.account.AccountDao
import com.ivy.core.persistence.entity.account.AccountEntity
import com.ivy.data.SyncState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AccountDaoFake: AccountDao {
    private val accounts = mutableListOf<AccountEntity>()
    val accountsReadOnly: List<AccountEntity>
        get() = accounts.toList()


    override suspend fun save(values: List<AccountEntity>) {
        // This will replace the list of accounts with the new values.
        // In a more complex implementation, you might want to merge or update the list instead.
        accounts.clear()
        accounts.addAll(values)
    }

    override suspend fun findAllBlocking(): List<AccountEntity> {
        // Not implemented for brevity. You can return a fixed list of accounts or implement more complex logic.
        return emptyList()
    }

    override suspend fun findAllOrdered(): List<AccountEntity> {
        // Returns accounts sorted by orderNum in ascending order.
        return accounts.sortedBy { it.orderNum }
    }

    override suspend fun findAllIds(): List<String> {
        // Returns just the IDs of all accounts.
        return accounts.map { it.id }
    }

    override fun findAll(): Flow<List<AccountEntity>> {
        // Returns a flow that emits the current list of accounts. It's a simple implementation and won't emit new values if accounts change.
        return flowOf(accounts.toList())
    }

    override suspend fun findById(accountId: String): AccountEntity? {
        // Finds an account by ID or returns null if not found.
        return accounts.find { it.id == accountId }
    }

    override suspend fun findMaxOrderNum(): Double? {
        // Returns the highest orderNum among all accounts or null if no accounts.
        return accounts.maxByOrNull { it.orderNum }?.orderNum
    }

    override suspend fun updateSync(accountId: String, sync: SyncState) {
        // Not implemented for brevity. You can find the account by ID and update its sync state.
    }
}
