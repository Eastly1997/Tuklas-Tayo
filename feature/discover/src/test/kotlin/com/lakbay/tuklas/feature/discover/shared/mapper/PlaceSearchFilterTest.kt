package com.lakbay.tuklas.feature.discover.shared.mapper

import com.lakbay.tuklas.feature.discover.domain.model.Place
import com.lakbay.tuklas.feature.discover.domain.model.PlaceCategory
import org.junit.Assert.*
import org.junit.Test

class PlaceSearchFilterTest {
    private val place = Place("sample", "Sample Kainan", PlaceCategory.RESTAURANT,
        "Some local street", "", city = "Malolos")

    @Test fun cityMatchesIndependentlyOfAddressAndIgnoresCaseAndQueryWhitespace() {
        for (query in listOf("Malolos", " mALoLoS ")) {
            assertEquals(listOf(place), filterPlacesForQuery(listOf(place), query))
        }
    }

    @Test fun missingBlankAndUnrelatedCitiesDoNotMatch() {
        val places = listOf(place.copy(city = null), place.copy(city = " "), place.copy(city = "Baliwag"))
        assertTrue(filterPlacesForQuery(places, "Malolos").isEmpty())
    }

    @Test fun existingMatchesAndEmptyQueryArePreserved() {
        for (query in listOf("KAINAN", "local street", "restaurant", " ")) {
            assertEquals(listOf(place), filterPlacesForQuery(listOf(place), query))
        }
        val cafe = place.copy(category = PlaceCategory.CAFE)
        for (query in listOf("cafe", "café")) assertEquals(listOf(cafe), filterPlacesForQuery(listOf(cafe), query))
    }
}
