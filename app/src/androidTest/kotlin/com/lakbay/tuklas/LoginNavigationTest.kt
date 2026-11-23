package com.lakbay.tuklas

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.lakbay.tuklas.auth.login.LoginViewModel
import kotlinx.coroutines.CompletableDeferred
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runners.model.Statement

/** Actual root graph with test-only auth DI; never invokes a real Google picker. */
class LoginNavigationTest {
    private val compose = createAndroidComposeRule<MainActivity>()
    private val resetAuth = TestRule { base, _ -> object : Statement() {
        override fun evaluate() { TestAuthRepository.reset(); base.evaluate() }
    } }
    @get:Rule val rules: RuleChain = RuleChain.outerRule(resetAuth).around(compose)
    @After fun cleanup() { compose.runOnUiThread { TestAuthRepository.reset() } }

    private fun openLogin() {
        compose.onNodeWithTag("tab_profile").performClick().assertIsSelected()
        compose.onNodeWithText("Browsing as Guest").assertIsDisplayed()
        compose.onNodeWithText("Sign In", useUnmergedTree = true).performClick()
        compose.onNodeWithText("Welcome to TuklasTayo").assertIsDisplayed()
    }

    private fun startExchange(gate: CompletableDeferred<Unit>) {
        compose.runOnUiThread {
            TestAuthRepository.exchangeGate = gate
            val activity = compose.activity
            ViewModelProvider(activity)[LoginViewModel::class.java]
                .signInWithGoogle(activity.lifecycleScope) { "test-token" }
        }
        compose.waitForIdle()
    }
    @Test fun separateLoginRestoresAcrossRecreationAndBackRevealsProfile() {
        compose.onNodeWithTag("tab_discover").assertIsSelected()
        compose.onNodeWithTag("tab_profile").performClick().assertIsSelected()
        compose.onNodeWithText("Sign In", useUnmergedTree = true).performClick()
        compose.onNodeWithText("Welcome to TuklasTayo").assertIsDisplayed()
        compose.onNodeWithTag("tab_profile").assertDoesNotExist()
        compose.onNodeWithText("Continue as Guest").assertDoesNotExist()
        compose.activityRule.scenario.recreate()
        compose.onNodeWithText("Welcome to TuklasTayo").assertIsDisplayed()
        compose.onNodeWithContentDescription("Back").performClick()
        compose.onNodeWithTag("tab_profile").assertIsSelected()
        compose.onNodeWithText("Manage Restaurants").assertDoesNotExist()
    }

    @Test fun successRestoresSelectedProfileAndSignOutWaitsForCredentialCleanup() {
        openLogin()
        val exchange = CompletableDeferred<Unit>()
        startExchange(exchange)
        compose.runOnUiThread { exchange.complete(Unit) }
        compose.onNodeWithTag("tab_profile").assertIsSelected()
        compose.onAllNodesWithText("Test Member").onFirst().assertIsDisplayed()
        val cleanup = CompletableDeferred<Unit>()
        compose.runOnUiThread { TestAuthRepository.cleanupGate = cleanup }
        compose.onNodeWithText("Sign Out").performScrollTo().performClick()
        compose.onNodeWithText("Signing out…").assertIsDisplayed()
        compose.onNodeWithText("Sign In").assertDoesNotExist()
        compose.onNodeWithTag("tab_profile").assertIsSelected()
        compose.runOnUiThread { cleanup.complete(Unit) }
        compose.onNodeWithText("Browsing as Guest").assertIsDisplayed()
        compose.onNodeWithText("Sign In").assertIsDisplayed()
    }

    @Test fun activeFirebaseExchangeSurvivesRecreationAndReturnsOnlyOnce() {
        openLogin()
        val exchange = CompletableDeferred<Unit>()
        startExchange(exchange)
        compose.activityRule.scenario.recreate()
        compose.onNodeWithText("Welcome to TuklasTayo").assertIsDisplayed()
        compose.runOnUiThread { exchange.complete(Unit) }
        compose.onNodeWithTag("tab_profile").assertIsSelected()
        compose.onAllNodesWithText("Test Member").onFirst().assertIsDisplayed()
        compose.activityRule.scenario.recreate()
        compose.onNodeWithTag("tab_profile").assertIsSelected()
        compose.onNodeWithText("Welcome to TuklasTayo").assertDoesNotExist()
    }

    @Test fun lateSuccessAfterLeavingLoginUpdatesProfileWithoutReopeningLogin() {
        openLogin()
        val exchange = CompletableDeferred<Unit>()
        startExchange(exchange)
        compose.onNodeWithContentDescription("Back").performClick()
        compose.onNodeWithTag("tab_profile").assertIsSelected()
        compose.runOnUiThread { exchange.complete(Unit) }
        compose.onAllNodesWithText("Test Member").onFirst().assertIsDisplayed()
        compose.onNodeWithTag("tab_profile").assertIsSelected()
        compose.onNodeWithText("Welcome to TuklasTayo").assertDoesNotExist()
    }
}
