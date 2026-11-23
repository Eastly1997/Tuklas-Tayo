package com.lakbay.tuklas.feature.discover.shared.mapper

import com.lakbay.tuklas.domain.model.Place
import com.lakbay.tuklas.domain.model.PlaceCategory
import com.lakbay.tuklas.feature.discover.shared.model.FeaturedShopUiModel

fun Place.toFeaturedShopUiModel() = FeaturedShopUiModel(
    id = id,
    name = name,
    imageUrl = imageUrl?.takeIf { it.isNotBlank() },
    category = category.displayLabel(),
    city = city?.trim()?.takeIf { it.isNotEmpty() },
    highlight = description.takeIf { it.isNotBlank() },
)
internal fun PlaceCategory.displayLabel(): String = when (this) {
    PlaceCategory.CAFE -> "Café"
    PlaceCategory.RESTAURANT -> "Restaurant"
    PlaceCategory.LOCAL_BUSINESS -> "Local business"
}