package com.lakbay.tuklas.feature.discover.domain.usecase

import com.lakbay.tuklas.feature.discover.domain.model.Place
import com.lakbay.tuklas.feature.discover.domain.repository.PlaceRepository

class GetFeaturedPlacesUseCase(private val repository: PlaceRepository) {
    suspend operator fun invoke(limit: Int = 10): List<Place> {
        require(limit >= 0) { "Limit must not be negative" }
        return if (limit == 0) emptyList() else repository.getFeaturedPlaces(limit)
    }
}

class GetPlaceByIdUseCase(private val repository: PlaceRepository) {
    suspend operator fun invoke(id: String): Place? {
        require(id.isNotBlank()) { "Place ID must not be blank" }
        return repository.getPlaceById(id)
    }
}
