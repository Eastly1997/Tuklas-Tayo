package com.lakbay.tuklas.feature.discover.profile

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.lakbay.tuklas.core.designsystem.theme.TuklasTheme
import com.lakbay.tuklas.feature.discover.navigation.DiscoverApp
import com.lakbay.tuklas.feature.discover.profile.ProfileAccount
import com.lakbay.tuklas.feature.discover.profile.ProfileScreen
import com.lakbay.tuklas.feature.discover.profile.ProfileUiState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {
    @get:Rule val compose = createComposeRule()
    @Test fun guestSignInIsOnlyANavigationCallback() {
        var requests = 0
        compose.setContent { TuklasTheme { ProfileScreen(ProfileUiState(), { requests++ }, {}) } }
        compose.onNodeWithText("Browsing as Guest").assertIsDisplayed()
        compose.onNodeWithText("Sign In").performClick()
        compose.runOnIdle { assertEquals(1, requests) }
        compose.onNodeWithText("Welcome to TuklasTayo").assertDoesNotExist()
        compose.onNodeWithText("Continue as Guest").assertDoesNotExist()
        compose.onNodeWithText("Manage Restaurants").assertDoesNotExist()
    }
    @Test fun selectedProfileSurvivesProjectedAccountChangesAndSignOut() {
        val state = mutableStateOf(ProfileUiState())
        compose.setContent { TuklasTheme { DiscoverApp(profileState = state.value, onSignInClick = {},
            onSignOutClick = { state.value = state.value.copy(account = null) }) } }
        compose.onNodeWithTag("tab_profile").performClick().assertIsSelected()
        compose.runOnIdle { state.value = state.value.copy(account = ProfileAccount(
            "Member",
            "member@example.test"
        )
        ) }
        compose.onNodeWithTag("tab_profile").assertIsSelected()
        compose.onNodeWithText("Sign Out").performScrollTo().performClick()
        compose.onNodeWithText("Browsing as Guest").assertExists()
        compose.onNodeWithTag("tab_profile").assertIsSelected()
    }
    @Test fun sessionResolutionDoesNotFlashGuestContent() {
        compose.setContent { TuklasTheme { ProfileScreen(ProfileUiState(isLoading = true), {}, {}) } }
        compose.onNodeWithText("Restoring your session…").assertIsDisplayed()
        compose.onNodeWithText("Browsing as Guest").assertDoesNotExist()
    }
    @Test fun signedOutSessionDoesNotOfferSignInUntilCleanupFinishes() {
        val state = mutableStateOf(ProfileUiState(isSigningOut = true))
        compose.setContent { TuklasTheme { ProfileScreen(state.value, {}, {}) } }
        compose.onNodeWithText("Signing out…").assertIsDisplayed()
        compose.onNodeWithText("Sign In").assertDoesNotExist()
        compose.runOnIdle { state.value = state.value.copy(isSigningOut = false, error = "Cleanup failed") }
        compose.onNodeWithText("Sign In").assertIsDisplayed()
        compose.onNodeWithText("Cleanup failed").assertIsDisplayed()
    }
}
