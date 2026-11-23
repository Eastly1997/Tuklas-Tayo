package com.lakbay.tuklas.feature.discover.domain.model

data class Place(
    val id: String,
    val name: String,
    val category: PlaceCategory,
    val address: String,
    val description: String,
    val imageUrl: String? = null,
    val city: String? = null,
)

enum class PlaceCategory { RESTAURANT, CAFE, LOCAL_BUSINESS }
