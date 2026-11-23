package com.lakbay.tuklas.feature.discover.shared.component

import org.junit.Assert.*
import org.junit.Test

class ExploreShopMetadataTest {
    @Test fun categoryAndCityUseOneMiddleDot() {
        assertEquals("Café · Malolos", exploreShopMetadata("Café", "Malolos"))
    }
    @Test fun missingCityLeavesCategoryOnly() {
        assertEquals("Restaurant", exploreShopMetadata("Restaurant", null))
        assertEquals("Restaurant", exploreShopMetadata("Restaurant", " "))
    }
    @Test fun missingCategoryLeavesCityOnly() {
        assertEquals("Malolos", exploreShopMetadata(null, "Malolos"))
        assertEquals("Malolos", exploreShopMetadata("", "Malolos"))
    }
    @Test fun absentValuesOmitMetadataEntirely() {
        assertNull(exploreShopMetadata(null, null))
        assertNull(exploreShopMetadata(" ", " "))
    }
    @Test fun surroundingWhitespaceIsTrimmedWithoutParsingAddresses() {
        assertEquals("Local business · Sample City", exploreShopMetadata(" Local business ", " Sample City "))
    }
}