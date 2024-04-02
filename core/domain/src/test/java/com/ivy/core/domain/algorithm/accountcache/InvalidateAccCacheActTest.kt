package com.ivy.core.domain.algorithm.accountcache

import arrow.core.NonEmptyList
import arrow.core.nonEmptyListOf
import com.ivy.common.time.provider.TimeProvider
import com.ivy.core.persistence.algorithm.accountcache.AccountCacheDao
import com.ivy.data.transaction.TrnTime
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId


class InvalidateAccCacheActTest {

    private lateinit var invalidateAccCacheAct: InvalidateAccCacheAct
    private lateinit var accountCacheDao: AccountCacheDao
    private lateinit var timeProvider: TimeProvider

    @BeforeEach
    fun setUp() {
        accountCacheDao = mockk(relaxed = true)
        timeProvider = mockk(relaxed = true)
        invalidateAccCacheAct = InvalidateAccCacheAct(accountCacheDao, timeProvider)

        // Mocking the timeProvider to return a fixed time for tests
        coEvery { timeProvider.timeNow() } returns LocalDateTime.of(2022, 10, 1, 12, 0)
        coEvery { timeProvider.dateNow() } returns LocalDate.of(2022, 10, 1)
        coEvery { timeProvider.zoneId() } returns ZoneId.systemDefault()
    }

    @Test
    fun `Invalidate cache on generic invalidate input`() = runBlocking {
        // Given an account ID
        val accountIds = nonEmptyListOf("account-1", "account-2")
        val input = InvalidateAccCacheAct.Input.Invalidate(accountIds)

        // When the action is invoked
        invalidateAccCacheAct(input)

        // Then the cache for all accounts should be invalidated
        accountIds.all.forEach { accountId ->
            coVerify { accountCacheDao.delete(accountId) }
        }
    }

    @Test
    fun `Ensure cache consistency on delete transaction`() = runBlocking {
        // Given a transaction deletion affecting an account
        val accountId = "test-account-id"
        val input = InvalidateAccCacheAct.Input.OnDeleteTrn(
            time = TrnTime.Actual(LocalDateTime.now()),
            accountIds = nonEmptyListOf(accountId)
        )

        // Assuming the cache time is after the transaction time, indicating inconsistency
        val cacheTime = Instant.now().plusSeconds(3600) // 1 hour after
        coEvery { accountCacheDao.findTimestampById(accountId) } returns cacheTime

        // When the action is invoked
        invalidateAccCacheAct(input)

        // Then the cache for the account should be invalidated due to inconsistency
        coVerify { accountCacheDao.delete(accountId) }
    }


    @Test
    fun `Invalidate cache on delete account`() = runBlocking {
        // Given an account ID
        val accountId = "test-account-id"
        val input = InvalidateAccCacheAct.Input.OnDeleteAcc(
            nonEmptyListOf(accountId)
        )

        // When the action is invoked
        invalidateAccCacheAct(input)

        // Then the cache for the account should be invalidated
        coVerify { accountCacheDao.delete(accountId) }
    }

    @Test
    fun `Ensure cache is invalidated on create transaction with future time`() = runBlocking {
        val accountId = "test-account-id"
        val futureTransactionTime = LocalDateTime.now().plusHours(1) // 1 hour in the future
        val input = InvalidateAccCacheAct.Input.OnCreateTrn(
            time = TrnTime.Actual(futureTransactionTime),
            accountIds = nonEmptyListOf(accountId)
        )

        // Assuming the cache time is before the transaction time
        val cacheTime = Instant.now().minusSeconds(3600) // 1 hour before
        coEvery { accountCacheDao.findTimestampById(accountId) } returns cacheTime

        invalidateAccCacheAct.action(input)

        coVerify { accountCacheDao.delete(accountId) }
    }

    @Test
    fun `Cache is invalidated when old transaction time is before and new time is after cache time`() = runBlocking {
        val accountId = "test-account-id"
        val oldTransactionTime = LocalDateTime.now().minusHours(2) // 2 hours in the past
        val newTransactionTime = LocalDateTime.now().plusHours(1) // 1 hour in the future
        val input = InvalidateAccCacheAct.Input.OnUpdateTrn(
            oldTime = TrnTime.Actual(oldTransactionTime),
            time = TrnTime.Actual(newTransactionTime),
            accountIds = nonEmptyListOf(accountId)
        )

        // Assuming the cache time is between the old and new transaction times
        val cacheTime = Instant.now().minusSeconds(3600) // 1 hour in the past
        coEvery { accountCacheDao.findTimestampById(accountId) } returns cacheTime

        invalidateAccCacheAct.action(input)

        coVerify { accountCacheDao.delete(accountId) }
    }
}