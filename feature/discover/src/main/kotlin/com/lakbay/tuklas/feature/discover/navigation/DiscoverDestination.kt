package com.lakbay.tuklas.feature.discover.navigation

import android.net.Uri

internal object DiscoverDestination {
    const val Discover = "discover"
    const val Saved = "saved"
    const val Profile = "profile"
    const val Search = "search"
    const val PlaceId = "placeId"
    const val PlaceDetail = "place/{placeId}"

    fun placeDetail(id: String): String = "place/${Uri.encode(id)}"
}

internal data class TopLevelDestination(
    val route: String,
    val labelRes: Int,
    val selectedIconRes: Int,
    val unselectedIconRes: Int,
)