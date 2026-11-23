package com.lakbay.tuklas.navigation

import android.net.Uri

internal object TuklasDestination {
    const val Discover = "discover"
    const val Saved = "saved"
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