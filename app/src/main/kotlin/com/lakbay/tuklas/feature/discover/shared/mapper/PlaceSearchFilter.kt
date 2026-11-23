package com.lakbay.tuklas.feature.discover.shared.mapper

import com.lakbay.tuklas.domain.model.Place

/** Existing local matching, shared by Discover and the dedicated Search screen. */
internal fun filterPlacesForQuery(places: List<Place>, rawQuery: String): List<Place> {
    val query = rawQuery.trim()
    return if (query.isBlank()) places else places.filter {
        it.name.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true) ||
            it.category.name.replace('_', ' ').contains(query, ignoreCase = true) ||
            it.toFeaturedShopUiModel().category.contains(query, ignoreCase = true)
    }
}