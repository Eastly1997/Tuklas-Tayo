package com.lakbay.tuklas.feature.discover.di

import com.lakbay.tuklas.feature.discover.data.repository.MockPlaceRepository
import com.lakbay.tuklas.feature.discover.domain.repository.PlaceRepository
import com.lakbay.tuklas.feature.discover.domain.usecase.GetFeaturedPlacesUseCase
import com.lakbay.tuklas.feature.discover.domain.usecase.GetPlaceByIdUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.koin.dsl.koinApplication

class DiscoverModulesTest {
    @Test fun mockGraphResolvesRepositoryAndBothUseCasesWithoutFirebase() = runTest {
        val application = koinApplication { modules(discoverModules(useFirebase = false)) }
        try {
            val repository = application.koin.get<PlaceRepository>()
            assertTrue(repository is MockPlaceRepository)
            val featured = application.koin.get<GetFeaturedPlacesUseCase>()(1)
            assertEquals(repository.getFeaturedPlaces(1), featured)
            assertNull(application.koin.get<GetPlaceByIdUseCase>()("missing-fixture"))
        } finally {
            application.close()
        }
    }
}
