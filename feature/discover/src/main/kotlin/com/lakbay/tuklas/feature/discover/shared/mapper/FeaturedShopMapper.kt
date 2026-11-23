package com.lakbay.tuklas.feature.discover.shared.mapper

import com.lakbay.tuklas.feature.discover.domain.model.Place
import com.lakbay.tuklas.feature.discover.domain.model.PlaceCategory
import com.lakbay.tuklas.feature.discover.shared.model.FeaturedShopUiModel
import com.lakbay.tuklas.feature.discover.R

fun Place.toFeaturedShopUiModel() = FeaturedShopUiModel(
    id = id,
    name = name,
    imageUrl = imageUrl?.takeIf { it.isNotBlank() },
    category = category.displayLabel(),
    city = city?.trim()?.takeIf { it.isNotEmpty() },
    highlight = description.takeIf { it.isNotBlank() },
    categoryResource = category.labelResource,
)
internal val PlaceCategory.labelResource: Int get() = when (this) {
    PlaceCategory.CAFE -> R.string.category_cafe
    PlaceCategory.RESTAURANT -> R.string.category_restaurant
    PlaceCategory.LOCAL_BUSINESS -> R.string.category_local_business
}

// Stable matching aliases preserve existing search behavior independently of UI localization.
internal fun PlaceCategory.displayLabel(): String = when (this) {
    PlaceCategory.CAFE -> "Café"
    PlaceCategory.RESTAURANT -> "Restaurant"
    PlaceCategory.LOCAL_BUSINESS -> "Local business"
}
