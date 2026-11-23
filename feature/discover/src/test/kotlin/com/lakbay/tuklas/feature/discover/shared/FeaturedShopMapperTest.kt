package com.lakbay.tuklas.feature.discover.shared

import com.lakbay.tuklas.feature.discover.shared.mapper.toFeaturedShopUiModel
import com.lakbay.tuklas.feature.discover.domain.model.Place
import com.lakbay.tuklas.feature.discover.domain.model.PlaceCategory
import org.junit.Assert.*
import org.junit.Test

class FeaturedShopMapperTest {
    @Test fun mapsStructuredCityWithoutExposingFullAddress() {
        val address = "123 Sample Street, Sample Barangay, Malolos, Bulacan"
        val place = Place("id", "Name", PlaceCategory.CAFE, address, "Coffee",
            "https://example.com/image.jpg", city = "  Malolos ")
        val shop = place.toFeaturedShopUiModel()
        assertEquals(place.id, shop.id)
        assertEquals(place.name, shop.name)
        assertEquals("Café", shop.category)
        assertEquals("Malolos", shop.city)
        assertEquals(address, place.address)
        assertEquals(place.description, shop.highlight)
        assertEquals(place.imageUrl, shop.imageUrl)
    }
    @Test fun missingCityDoesNotGuessFromAddress() {
        val shop = Place("id", "Name", PlaceCategory.CAFE,
            "123 Sample Street, Malolos, Bulacan", "Coffee").toFeaturedShopUiModel()
        assertNull(shop.city)
    }
    @Test fun blankOptionalContentIsOmitted() {
        val shop = Place("id", "Name", PlaceCategory.LOCAL_BUSINESS, "", " ", "", city = " ").toFeaturedShopUiModel()
        assertNull(shop.highlight)
        assertNull(shop.imageUrl)
        assertNull(shop.city)
    }
}