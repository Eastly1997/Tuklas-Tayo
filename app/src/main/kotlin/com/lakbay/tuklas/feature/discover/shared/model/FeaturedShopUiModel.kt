package com.lakbay.tuklas.feature.discover.shared.model

/** Display-only projection of a domain Place. Resource IDs are reserved for offline debug previews. */
data class FeaturedShopUiModel(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val category: String,
    val city: String? = null,
    val highlight: String? = null,
    val imageResource: Int? = null,
)