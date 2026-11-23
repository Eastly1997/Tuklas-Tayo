package com.lakbay.tuklas.data.dto

import com.lakbay.tuklas.domain.model.PlaceCategory
import org.junit.Assert.*
import org.junit.Test

class PlaceDtoTest {
    @Test fun `maps document ID and supported category without Firebase types`() {
        val place = PlaceDto(name = "Sample", category = "CAFE", address = "Malolos").toDomain("sample")
        assertEquals("sample", place.id)
        assertEquals(PlaceCategory.CAFE, place.category)
        assertEquals("Malolos", place.address)
        assertNull(place.imageUrl)
    }

    @Test fun `unknown categories fall back to local business`() {
        assertEquals(PlaceCategory.LOCAL_BUSINESS,
            PlaceDto(name = "Sample", category = "NEW_CATEGORY").toDomain("sample").category)
    }

    @Test fun structuredCityIsOptionalAndPreservesAddress() {
        val address = "123 Sample Street, Sample Barangay, Malolos, Bulacan"
        val place = PlaceDto(name = "Sample", address = address, city = " Malolos ").toDomain("sample")
        assertEquals("Malolos", place.city)
        assertEquals(address, place.address)
        assertNull(PlaceDto(name = "Sample", address = address).toDomain("sample").city)
        assertNull(PlaceDto(name = "Sample", city = " ").toDomain("sample").city)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `malformed document is rejected`() { PlaceDto().toDomain("sample") }
}
