# Ivy Wallet Test Deployment Guideline

Ivy Wallet is a free money manager android app written using 100% Jetpack Compose and Kotlin. It's designed to help you track your personal finance with ease.

## Installation

Use the Android Studio [link](https://developer.android.com/studio) to install Android Studio.


## After Installation
- Launch Android Studio Application
- Click on File in the Menu Bar
- Click on Open
- Locate Project Directory 
- Click on Project
- Wait for the project to Load
- Click Trust Project From the Dialog box 
- Wait for gradle files to be Downloaded and Initialized

## Running Unit Tests

### First Set of Unit Tests
- Navigate to the core module.
- Click the core module.
- Click the domain folder in the core module.
- Click the kotlin+java folder in the domain folder.
- Left-click or double-finger-click the 'com.ivy (test)' folder. 
- Select 'Run Test in com.ivy' to run all unit tests in the domain module.


### Second Set of Unit Tests
- Navigate to the core module.
- Click the core module.
- Click the exchange-rates folder in the core module.
- Click the kotlin+java folder in the exchange-rates folder.
- Left-click or double-finger-click the 'com.ivy.exchangeRates (test)' folder. 
- Select 'Run Test in com.ivy.ex..' to run all unit tests in the exchangeRates module.


### Third Set of Unit Tests
- Navigate to the core module.
- Click the math module.
- Click the kotlin+java folder in the math folder.
- Left-click or double-finger-click the 'com.ivy.math (test)' folder. 
- Select 'Run Test in com.ivy.ma..' to run all unit tests in the math module.




## Running Integration / Instrumented Tests

### First Instrumented Test
- Navigate to the core module.
- Click the core module.
- Click the domain folder in the core module.
- Click the kotlin+java folder in the domain folder.
- Left-click or double-finger-click the 'com.ivy.core.domain.action.account (androidTest)' folder. 
- Select 'Run Test in com.ivy.co..' to run the integration test in the domain module.


### Second Instrumented Test
- Navigate to the core module.
- Click the core module.
- Click the ui folder in the core module.
- Click the kotlin+java folder in the ui folder.
- Left-click or double-finger-click the 'com.ivy.core.ui.time.picker.date (androidTest)' folder. 
- Select 'Run Test in com.ivy.co..' to run the integration test in the ui module.



## Running UI Tests 

Connect to your phone or launch a simulator to execute the UI test.

### First UI Test
- Navigate to the app module.
- Click the app module.
- Click the kotlin+java folder in the app module.
- Click 'com.ivy (androidTest)' folder
- Click the home folder 
- Left-click or double-finger-click the HomeScreenTest. 
- Select 'Run HomeScreenTest' to run the UI test.


### Second UI Test
- Navigate to the app module.
- Click the app module.
- Click the kotlin+java folder in the app module.
- Click 'com.ivy (androidTest)' folder.
- Click the account folder. 
- Left-click or double-finger-click the AccountScreenTest. 
- Select 'Run HomeScreenTest' to run the UI test.



## Running End to End Tests 
- Navigate to the app module.
- Click the app module.
- Click the kotlin+java folder in the app module.
- Click 'com.ivy (androidTest)' folder.
- Left-click or double-finger-click the CreateTransactionE2E. 
- Select 'Run CreateTransactionE2E' to run the UI test.

