package com.ivy.account

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.ivy.common.androidtest.IvyAndroidTest
import com.ivy.navigation.Navigator
import com.ivy.navigation.destinations.main.Accounts
import com.ivy.navigation.destinations.main.Home
import com.ivy.transaction.NewTransactionRobot
import com.ivy.wallet.ui.RootActivity
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AccountScreenTest:IvyAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<RootActivity>()

    @Inject
    lateinit var navigator: Navigator

    @Test
    fun testCreatingAccount() = runBlocking<Unit> {
        composeRule.awaitIdle()
        composeRule.runOnUiThread {
            navigator.navigate(Accounts.route)
        }


        composeRule.onNodeWithText("Create account").performClick()
        composeRule.onNodeWithContentDescription("New account")
            .performTextInput("Paypal")
        composeRule.onAllNodesWithText("Add account").onLast().performClick()


        composeRule.onNodeWithContentDescription("Paypal").assertIsDisplayed()
    }


}