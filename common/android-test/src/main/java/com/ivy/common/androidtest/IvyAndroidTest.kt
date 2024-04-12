package com.ivy.common.androidtest

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ApplicationProvider
import com.ivy.common.time.provider.TimeProvider
import com.ivy.core.persistence.IvyWalletCoreDb
import com.ivy.core.persistence.datastore.dataStore
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.time.LocalDate
import javax.inject.Inject

abstract class IvyAndroidTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this) // This is the hilt reference for testing with hilt

    @Inject
    lateinit var db: IvyWalletCoreDb

    @Inject
    lateinit var timeProvider: TimeProvider

    protected lateinit var context: Context //This is the reference to the context

    @Before
    open fun setUp() { // This is the test setup function
        context = ApplicationProvider.getApplicationContext()
        hiltRule.inject() //This line is used to inject all dependencies
        db.clearAllTables() // this is to clear our database
        clearDataStore()
    }

    @After
    open fun tearDown() {
        db.close() // This is to close our database connection
    }

    protected fun setDate(date: LocalDate) {
        (timeProvider as TimeProviderFake).apply {
            timeNow = date.atTime(12, 0)
            dateNow = date
        }
    }

    private fun clearDataStore() = runBlocking {
        context.dataStore.edit {
            it.clear()
        }
    }
}