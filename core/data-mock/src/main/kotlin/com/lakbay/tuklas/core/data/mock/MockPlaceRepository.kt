package com.lakbay.tuklas.core.data.mock

import com.lakbay.tuklas.core.domain.Place
import com.lakbay.tuklas.core.domain.PlaceRepository

class MockPlaceRepository(places: List<Place> = emptyList()) : PlaceRepository {
    private val places = places.toList()
    override suspend fun getFeaturedPlaces(limit: Int): List<Place> {
        require(limit >= 0) { "Limit must not be negative" }
        return places.take(limit)
    }

    override suspend fun getPlaceById(id: String): Place? {
        require(id.isNotBlank()) { "Place ID must not be blank" }
        return places.find { it.id == id }
    }
}
