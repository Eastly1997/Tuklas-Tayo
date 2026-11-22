package com.lakbay.tuklas.feature.discover.presentation.mapper

import com.lakbay.tuklas.core.domain.Place
import com.lakbay.tuklas.core.domain.PlaceCategory
import com.lakbay.tuklas.feature.discover.presentation.model.FeaturedShopUiModel

fun Place.toFeaturedShopUiModel() = FeaturedShopUiModel(
    id = id,
    name = name,
    imageUrl = imageUrl?.takeIf { it.isNotBlank() },
    category = when (category) {
        PlaceCategory.CAFE -> "Café"
        PlaceCategory.RESTAURANT -> "Restaurant"
        PlaceCategory.LOCAL_BUSINESS -> "Local business"
    },
    city = city?.trim()?.takeIf { it.isNotEmpty() },
    highlight = description.takeIf { it.isNotBlank() },
)