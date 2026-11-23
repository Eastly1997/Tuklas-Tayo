package com.lakbay.tuklas.domain.repository

import com.lakbay.tuklas.domain.model.Place

interface PlaceRepository {
    /** Returns at most [limit] places in a stable, backend-defined featured order.
     * Zero returns an empty list; negative limits are invalid. Failures throw exceptions.
     */
    suspend fun getFeaturedPlaces(limit: Int): List<Place>

    /** Returns null when absent. Blank IDs are invalid; backend failures throw exceptions. */
    suspend fun getPlaceById(id: String): Place?
}
