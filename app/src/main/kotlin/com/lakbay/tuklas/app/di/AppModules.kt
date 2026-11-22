package com.lakbay.tuklas.app.di

import com.google.firebase.firestore.FirebaseFirestore
import com.lakbay.tuklas.core.data.firebase.FirestorePlaceRepository
import com.lakbay.tuklas.core.data.mock.MockPlaceRepository
import com.lakbay.tuklas.core.domain.GetFeaturedPlacesUseCase
import com.lakbay.tuklas.core.domain.GetPlaceByIdUseCase
import com.lakbay.tuklas.core.domain.PlaceRepository
import com.lakbay.tuklas.feature.discover.di.discoverModule
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

fun appModules(useFirebase: Boolean) = listOf(domainModule, discoverModule) +
    if (useFirebase) listOf(firebaseDependenciesModule, firebaseRepositoryModule)
    else listOf(mockRepositoryModule)
