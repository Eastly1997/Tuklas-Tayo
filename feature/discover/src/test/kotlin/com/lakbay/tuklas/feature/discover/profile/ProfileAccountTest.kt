package com.lakbay.tuklas.feature.discover.profile

import com.lakbay.tuklas.feature.discover.presentation.profile.ProfileAccount
import com.lakbay.tuklas.feature.discover.presentation.profile.profileEmail
import com.lakbay.tuklas.feature.discover.presentation.profile.profileName
import com.lakbay.tuklas.feature.discover.presentation.profile.profilePhoto
import org.junit.Assert.*
import org.junit.Test

class ProfileAccountTest {
    @Test fun missingAccountFieldsHaveNeutralFallbackAndNoFakeValues() {
        val user = ProfileAccount(null)
        assertNull(user.profileName)
        assertNull(user.profileEmail); assertNull(user.profilePhoto)
    }
    @Test fun providedFirebaseFieldsArePreservedAndTrimmedForDisplay() {
        val user =
            ProfileAccount(" Member ", " member@example.test ", " https://example.test/avatar.png ")
        assertEquals("Member", user.profileName)
        assertEquals("member@example.test", user.profileEmail)
        assertEquals("https://example.test/avatar.png", user.profilePhoto)
    }
    @Test fun blankValuesAreNotRenderedAsAccountDetails() {
        val user = ProfileAccount(" ", " ", " ")
        assertNull(user.profileName)
        assertNull(user.profileEmail); assertNull(user.profilePhoto)
    }
}