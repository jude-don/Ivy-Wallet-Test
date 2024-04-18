package com.ivy.transaction

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.ivy.IvyComposeRule
import com.ivy.core.data.CategoryType

// A utility class for automating UI interactions related to adding and managing new transactions using IvyComposeRule.
class NewTransactionRobot(
    private val composeRule: IvyComposeRule
) {
    // Adds a new account with the specified name by interacting with UI elements labeled for adding accounts.
    fun addAccount(name: String): NewTransactionRobot {
        composeRule.onNodeWithText("Add account").performClick()
        composeRule.onNodeWithContentDescription("New account").performTextInput(name)
        composeRule.onAllNodesWithText("Add account").onLast().performClick()
        return this
    }

    // Selects an existing account by name from the list of accounts.
    fun selectAccount(name: String): NewTransactionRobot {
        composeRule.onNodeWithText(name).performClick()
        return this
    }

    // Enters a transaction amount by simulating clicks on the numeric keypad UI for each digit in the amount.
    fun enterTransactionAmount(amount: Int): NewTransactionRobot {
        val digits = amount.toString().map { it.digitToInt() }
        digits.forEach { digit ->
            composeRule.onNode(
                hasText(digit.toString()) and hasClickAction()
            ).performClick()
        }
        composeRule.onNodeWithText("Enter").performClick()
        return this
    }

    // Adds a category for the transaction with optional parenting, using modal dialogs for input.
    fun addCategory(name: String, type: CategoryType, parentName: String?): NewTransactionRobot {
        clickAddCategoryOnNewCategoryModal()
            .enterCategoryName(name)
            .selectCategoryType(type)
            .apply {
                if(parentName != null) {
                    chooseParent(parentName)
                }
            }
            .clickAddCategoryOnNewCategoryModal()
        return this
    }

    // Helper method to initiate adding a category from the modal dialog.
    private fun clickAddCategoryOnNewCategoryModal(): NewTransactionRobot {
        composeRule.onAllNodesWithText("Add category").onLast().performClick()
        return this
    }

    // Enters the name of a new category in the input field.
    private fun enterCategoryName(name: String): NewTransactionRobot {
        composeRule.onNodeWithContentDescription("New Category").performTextInput(name)
        return this
    }

    // Selects the category type from a set of options.
    private fun selectCategoryType(type: CategoryType): NewTransactionRobot {
        composeRule.onNode(
            hasText(type.toString()) and hasTestTag("category_type_button")
        ).performClick()
        return this
    }

    // Chooses the parent category during the category addition process.
    private fun chooseParent(parentName: String): NewTransactionRobot {
        composeRule.onNodeWithText("Choose parent").performClick()
        composeRule.onAllNodesWithText(parentName).onLast().performClick()
        return this
    }

    // Chooses a sub-category under a given parent category by selecting from nested categories.
    fun chooseSubCategory(parentName: String, subName: String): NewTransactionRobot {
        composeRule.onNodeWithText("Unspecified").performClick()
        //composeRule.onNodeWithText(subName).performClick()
        return this
    }

    // Enters the title of the transaction.
    fun enterTransactionTitle(title: String): NewTransactionRobot {
        composeRule.onNodeWithContentDescription("Title").performClick()
        composeRule.onNodeWithContentDescription("Title").performTextInput(title)
        return this
    }

    // Adds a description to the transaction by interacting with the description input area.
    fun enterTransactionDescription(description: String): NewTransactionRobot {
        composeRule.onNodeWithText("Add description").performClick()
        composeRule
            .onNodeWithContentDescription("Enter any details here")
            .performClick()
            .performTextInput(description)
        composeRule.onAllNodesWithText("Add").onLast().performClick()
        return this
    }

    // Confirms the addition of a new transaction by clicking the "Add" button.
    fun clickAddTransaction(): NewTransactionRobot {
        composeRule.onNodeWithText("Add").performClick()
        return this
    }
}
