package com.lakbay.tuklas.data.dto

import com.lakbay.tuklas.domain.model.Place
import com.lakbay.tuklas.domain.model.PlaceCategory

// Defaults support Firestore deserialization. Document IDs are mapped separately.
internal data class PlaceDto(
    var name: String = "",
    var category: String = "LOCAL_BUSINESS",
    var address: String = "",
    var description: String = "",
    var imageUrl: String? = null,
    var featured: Boolean = false,
    var city: String? = null,
) {
    fun toDomain(id: String): Place {
        require(id.isNotBlank() && name.isNotBlank()) { "Invalid place document" }
        return Place(
            id = id,
            name = name,
            category = PlaceCategory.entries.find { it.name == category } ?: PlaceCategory.LOCAL_BUSINESS,
            address = address,
            description = description,
            imageUrl = imageUrl,
            city = city?.trim()?.takeIf { it.isNotEmpty() },
        )
    }
}
