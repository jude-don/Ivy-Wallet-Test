package com.ivy.core.domain.action.account

import app.cash.turbine.test
import app.cash.turbine.timeout
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.ivy.common.androidtest.TimeProviderFake
import com.ivy.common.androidtest.test_data.accountEntityParam
import com.ivy.common.time.provider.TimeProvider
import com.ivy.core.domain.action.exchange.MainCoroutineExtension
import com.ivy.core.domain.action.exchange.TestDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

//@OptIn(ExperimentalCoroutinesApi::class)
//class AccountsFlowTest{
//    private lateinit var accountsFlow: AccountsFlow
//    private lateinit var accountDaoFake: AccountDaoFake
//    private lateinit var timeProvider: TimeProviderFake
//
//    companion object {
//        @JvmField
//        @RegisterExtension
//        val mainCoroutineExtension = MainCoroutineExtension()
//    }
//
//
//    @BeforeEach
//    fun setup(){
//        accountDaoFake = AccountDaoFake()
//        timeProvider = TimeProviderFake()
//
//        val testDispatchers = TestDispatchers(mainCoroutineExtension.testDispatcher)
//
//        accountsFlow = AccountsFlow(accountDaoFake,timeProvider)
//    }
//
//    @OptIn(ExperimentalTime::class)
//    @Test
//    fun `Test accounts flow emissions`() = runTest{
//
//        val acc1 = accountEntityParam(ordernum = 1.0, name = "Checking", currency = "USD")
//        val acc2 = accountEntityParam(ordernum = 2.0, name = "Savings", currency = "EUR")
//        val accounts = listOf(
//            acc1,
//            acc2
//        )
//
//        accountsFlow.invoke().test(timeout= 15.seconds){
//            awaitItem() // Initial emission, ignore
//
//            accountDaoFake.save(accounts)
//            val accountsList = awaitItem()
//
//            // Assert the number of accounts and their properties
//            assertThat(accountsList).hasSize(2)
//            assertThat(accountsList.first().id).isEqualTo(acc1.id)
//
//            cancelAndIgnoreRemainingEvents()
//            //awaitComplete()
//        }
//    }
//}
class AccountsFlowTest {

    private lateinit var accountDaoFake: AccountDaoFake
    private lateinit var timeProvider: TimeProviderFake
    private lateinit var accountsFlow: AccountsFlow

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {

        accountDaoFake = AccountDaoFake()
        timeProvider = TimeProviderFake()
        // Set the main dispatcher to Unconfined for testing purposes
        Dispatchers.setMain(Dispatchers.Unconfined)

        // Initialize AccountsFlow with mocked dependencies
        accountsFlow = AccountsFlow(accountDaoFake, timeProvider)

        // Setup mocks
//        coEvery { accountDao.findAll() } returns flowOf(listOf(
//            AccountEntity("1", "Account 1", /* other properties */),
//            AccountEntity("2", "Account 2", /* other properties */)
//        ))

        // Mock the timeProvider if necessary, for example:
        // every { timeProvider.timeNow() } returns LocalDateTime.now()
    }

    @AfterEach
    fun tearDown() {
        // Reset the main dispatcher after the test
        Dispatchers.resetMain()
    }

    @Test
    fun `AccountsFlow emits correct accounts list`() = runBlocking {
        // Collect the first emission from the AccountsFlow
        val accounts = accountsFlow().first()

        // Verify the transformation logic
        // Here we assume `toDomain` simply maps AccountEntity to Account with the same id and name
        assertEquals(0, accounts.size)
        assertEquals("1", accounts[0].id)
        assertEquals("Account 1", accounts[0].name)
        assertEquals("2", accounts[1].id)
        assertEquals("Account 2", accounts[1].name)

        // Add more assertions as necessary to validate the transformation logic
    }
}