package com.lakbay.tuklas.core.data.mock

import com.lakbay.tuklas.core.domain.Place
import com.lakbay.tuklas.core.domain.PlaceCategory

// Fictional development fixtures, not recommendations or actual business listings.
internal val samplePlaces = listOf(
    Place("sample-kainan", "Sample Kainan", PlaceCategory.RESTAURANT,
        "Fictional address, Malolos, Bulacan", "Fictional restaurant for development only."),
    Place("sample-kape", "Sample Kape", PlaceCategory.CAFE,
        "Fictional address, Malolos, Bulacan", "Fictional cafe for development only."),
    Place("sample-lokal", "Sample Lokal Shop", PlaceCategory.LOCAL_BUSINESS,
        "Fictional address, Malolos, Bulacan", "Fictional local business for development only."),
)
