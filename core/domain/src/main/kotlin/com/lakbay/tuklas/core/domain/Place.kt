package com.lakbay.tuklas.core.domain

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
