package com.lakbay.tuklas.di

import com.google.firebase.firestore.FirebaseFirestore
import com.lakbay.tuklas.data.repository.FirestorePlaceRepository
import com.lakbay.tuklas.data.repository.MockPlaceRepository
import com.lakbay.tuklas.domain.usecase.GetFeaturedPlacesUseCase
import com.lakbay.tuklas.domain.usecase.GetPlaceByIdUseCase
import com.lakbay.tuklas.domain.repository.PlaceRepository
import com.lakbay.tuklas.feature.discover.explore.AnnouncementUiModel
import com.lakbay.tuklas.feature.discover.explore.ExploreViewModel
import com.lakbay.tuklas.feature.discover.search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mockRepositoryModule = module {
    single<PlaceRepository> { MockPlaceRepository(developmentPlaces) }
}

val firebaseDependenciesModule = module {
    single { FirebaseFirestore.getInstance() }
}

val firebaseRepositoryModule = module {
    single<PlaceRepository> { FirestorePlaceRepository(get()) }
}

val domainModule = module {
    factory { GetFeaturedPlacesUseCase(get()) }
    factory { GetPlaceByIdUseCase(get()) }
}

val presentationModule = module {
    viewModel {
        ExploreViewModel(get(), initialAnnouncement = AnnouncementUiModel(
            "tuklas", "Welcome to TuklasTayo!", "Discover local favorites around Malolos.",
        ))
    }
    viewModel { SearchViewModel(get(), get()) }
}

fun appModules(useFirebase: Boolean) = listOf(domainModule, presentationModule) +
    if (useFirebase) listOf(firebaseDependenciesModule, firebaseRepositoryModule)
    else listOf(mockRepositoryModule)
