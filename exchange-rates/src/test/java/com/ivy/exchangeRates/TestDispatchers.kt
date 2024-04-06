package com.ivy.exchangeRates

import com.ivy.core.domain.pure.util.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
internal class TestDispatchers(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
): DispatcherProvider {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val main: CoroutineDispatcher
        get() = testDispatcher
    @OptIn(ExperimentalCoroutinesApi::class)
    override val io: CoroutineDispatcher
        get() = testDispatcher
    @OptIn(ExperimentalCoroutinesApi::class)
    override val default: CoroutineDispatcher
        get() = testDispatcher
    @OptIn(ExperimentalCoroutinesApi::class)
    override val unconfined: CoroutineDispatcher
        get() = testDispatcher
}