package com.lakbay.tuklas.feature.discover.di

import com.google.firebase.firestore.FirebaseFirestore
import com.lakbay.tuklas.feature.discover.data.repository.FirestorePlaceRepository
import com.lakbay.tuklas.feature.discover.data.repository.MockPlaceRepository
import com.lakbay.tuklas.feature.discover.domain.usecase.GetFeaturedPlacesUseCase
import com.lakbay.tuklas.feature.discover.domain.usecase.GetPlaceByIdUseCase
import com.lakbay.tuklas.feature.discover.domain.repository.PlaceRepository
import com.lakbay.tuklas.feature.discover.explore.AnnouncementUiModel
import com.lakbay.tuklas.feature.discover.explore.ExploreViewModel
import com.lakbay.tuklas.feature.discover.search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import com.lakbay.tuklas.feature.discover.development.developmentPlaces
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext
import com.lakbay.tuklas.feature.discover.R

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
            "tuklas", androidContext().getString(R.string.announcement_welcome_title),
            androidContext().getString(R.string.announcement_welcome_message),
        ))
    }
    viewModel { SearchViewModel(get(), get()) }
}

fun discoverModules(useFirebase: Boolean) = listOf(domainModule, presentationModule) +
    if (useFirebase) listOf(firebaseDependenciesModule, firebaseRepositoryModule)
    else listOf(mockRepositoryModule)
