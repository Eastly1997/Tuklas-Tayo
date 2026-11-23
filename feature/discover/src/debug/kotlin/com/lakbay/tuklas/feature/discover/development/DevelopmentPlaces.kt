package com.lakbay.tuklas.feature.discover.development

import com.lakbay.tuklas.feature.discover.R
import com.lakbay.tuklas.feature.discover.domain.model.Place
import com.lakbay.tuklas.feature.discover.domain.model.PlaceCategory

// Fictional development fixtures, not recommendations or actual business listings.
val developmentPlaces = listOf(
    Place("sample-kainan", "Sample Kainan", PlaceCategory.RESTAURANT,
        "Fictional address, Malolos, Bulacan", "Fictional restaurant for development only.", "android.resource://com.lakbay.tuklas/${R.drawable.featured_sample_cafe}", city = "Malolos"),
    Place("sample-kape", "Sample Kape", PlaceCategory.CAFE,
        "Fictional address, Malolos, Bulacan", "Coffee and cozy afternoons — fictional sample.", "android.resource://com.lakbay.tuklas/${R.drawable.featured_sample_coffee}", city = "Malolos"),
    Place("sample-lokal", "Sample Lokal Shop", PlaceCategory.LOCAL_BUSINESS,
        "Fictional address, Malolos, Bulacan", "Fictional local business for development only.", city = "Malolos"),
)

const val developmentDataNotice = "Development samples — fictional businesses and illustrative stock photographs."
