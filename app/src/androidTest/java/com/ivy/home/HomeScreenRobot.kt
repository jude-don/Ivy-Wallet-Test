package com.ivy.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.ivy.IvyComposeRule
import com.ivy.common.time.provider.TimeProvider
import com.ivy.data.CurrencyCode
import com.ivy.navigation.Navigator
import com.ivy.navigation.destinations.main.Home
import kotlinx.coroutines.runBlocking

class HomeScreenRobot( // Represents a UI testing robot for the Home screen using IvyComposeRule to manage test operations.
    private val composeRule: IvyComposeRule
) {
    fun navigateTo(navigator: Navigator): HomeScreenRobot { // Navigates to the Home screen using the provided Navigator object. This method blocks until the UI thread is idle.

        runBlocking {
            composeRule.awaitIdle()
            composeRule.runOnUiThread {
                navigator.navigate(Home.route) {
                    popUpTo(Home.route) {
                        inclusive = false
                    }
                }
            }
        }
        return this
    }

    fun openDateRangeSheet(timeProvider: TimeProvider): HomeScreenRobot {  // Opens the date range selection sheet by clicking on the current month's name. Utilizes the TimeProvider to get the current date.

        composeRule
            .onNodeWithText(timeProvider.dateNow().month.name, ignoreCase = true)
            .performClick()
        return this
    }

    fun selectMonth(monthName: String): HomeScreenRobot {  // Selects a specific month by clicking on a text node that matches the given month name.

        composeRule
            .onNodeWithText(monthName)
            .performClick()
        return this
    }

    fun assertDateIsDisplayed(day: Int, month: String): HomeScreenRobot {  // Asserts that a specific date (day and abbreviated month) is displayed on the screen.
        val paddedDay = day.toString().padStart(2, '0')
        composeRule
            .onNodeWithText("${month.take(3)}. $paddedDay")
            .assertIsDisplayed()
        return this
    }

    fun clickDone(): HomeScreenRobot { // Performs a click action on the "Done" button.
        composeRule.onNodeWithText("Done").performClick()
        return this
    }

    fun clickUpcoming(): HomeScreenRobot { // Clicks on the "Upcoming" button to presumably filter or display upcoming events or transactions.
        composeRule.onNodeWithText("Upcoming").performClick()
        return this
    }

    fun assertTransactionDoesNotExist(transactionTitle: String): HomeScreenRobot { // Asserts that a transaction with the specified title does not exist in the current view.
        composeRule.onNodeWithText(transactionTitle).assertDoesNotExist()
        return this
    }

    fun assertTransactionIsDisplayed(transactionTitle: String): HomeScreenRobot { // Asserts that a transaction with the specified title, account name, and category name are all displayed on the screen.
        composeRule.onNodeWithText(transactionTitle).assertIsDisplayed()
        return this
    }

    fun assertTransactionIsDisplayed( // Asserts that a transaction with the specified title is displayed on the screen.
        transactionTitle: String,
        accountName: String,
        categoryName: String
    ): HomeScreenRobot {
        composeRule.onNodeWithText(transactionTitle).assertIsDisplayed()
        composeRule.onNodeWithText(accountName).assertIsDisplayed()
        //composeRule.onNodeWithText(categoryName).assertIsDisplayed()
        return this
    }

    fun openOverdue(): HomeScreenRobot { // Clicks on the "Overdue" button to view overdue transactions or tasks.
        composeRule
            .onNodeWithText("Overdue")
            .performClick()
        return this
    }

    fun assertBalanceIsDisplayed(amount: Double, currency: CurrencyCode): HomeScreenRobot { // Asserts that the specified balance amount and currency are displayed on the screen, handling both integer and floating-point representations.
        val formattedAmount = if(amount % 1.0 == 0.0) {
            amount.toInt().toString()
        } else amount.toString()

        composeRule
            .onAllNodes(
                hasText(formattedAmount) and hasAnySibling(hasText(currency)),
                useUnmergedTree = true
            )
            .onFirst()
            .assertIsDisplayed()

        return this
    }

    fun clickGet(): HomeScreenRobot { // Performs a click action on the "Get" button, potentially to retrieve or display additional information.
        composeRule.onNodeWithText("Get").performClick()
        return this
    }

    fun clickNewTransaction(): HomeScreenRobot { // Initiates the creation of a new transaction by clicking on an interactive element labeled with "Add new transaction."
        composeRule.onNodeWithContentDescription("Add new transaction").performClick()
        return this
    }

    fun clickExpense(): HomeScreenRobot { // Triggers the creation of a new expense entry by interacting with the "Create new expense" element.
        composeRule.onNodeWithContentDescription("Create new expense").performClick()
        return this
    }

    fun assertTotalExpensesIs(amount: Int): HomeScreenRobot { // Asserts that the total displayed expenses match the provided amount.
        composeRule
            .onAllNodesWithTag("amount", useUnmergedTree = true)
            .onLast()
            .assertTextEquals(amount.toString())
        return this
    }

}