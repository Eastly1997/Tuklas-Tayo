package com.lakbay.tuklas.feature.discover.explore

/** One optional presentation announcement, with no backend schema or runtime dependency. */
data class AnnouncementUiModel(
    val id: String,
    val title: String,
    val description: String? = null,
    val destination: AnnouncementDestination? = null,
    val isActive: Boolean = true,
)

sealed interface AnnouncementDestination {
    data class PlaceDetail(val placeId: String) : AnnouncementDestination {
        init { require(placeId.isNotBlank()) }
    }
}