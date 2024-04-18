//package com.ivy.categories
//
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.compose.ui.test.junit4.createComposeRule
//import org.junit.Rule
//import org.junit.Test
//
//class CategoriesScreenTest {
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @OptIn(ExperimentalComposeUiApi::class)
//    @Test
//    fun testUIRendering() {
//        // Launch the screen
//        composeTestRule.setContent {
//            CategoriesScreen()
//        }
//
//        // Verify that the header is displayed
//        composeTestRule.onNodeWithTag("header").assertIsDisplayed()
//
//        // Verify that the "Add" button is displayed and click it
//        composeTestRule.onNodeWithText("Add").assertIsDisplayed().performClick()
//
//        // Verify that the period modal is displayed
//        composeTestRule.onNodeWithText("Period Modal Title").assertIsDisplayed()
//    }
//}