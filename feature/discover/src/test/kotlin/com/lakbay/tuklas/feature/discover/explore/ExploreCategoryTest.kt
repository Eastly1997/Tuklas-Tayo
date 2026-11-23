package com.lakbay.tuklas.feature.discover.explore

import com.lakbay.tuklas.feature.discover.domain.model.*
import com.lakbay.tuklas.feature.discover.domain.repository.*
import com.lakbay.tuklas.feature.discover.domain.usecase.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExploreCategoryTest {
    private val dispatcher = StandardTestDispatcher()
    private val cafe = Place("cafe", "Sample Coffee", PlaceCategory.CAFE, "Sample City", "")
    private val shop = Place("shop", "Sample Shop", PlaceCategory.LOCAL_BUSINESS, "Sample City", "")
    private class FakeRepository(val places: List<Place>) : PlaceRepository {
        var calls = 0
        var gate: CompletableDeferred<Unit>? = null
        override suspend fun getFeaturedPlaces(limit: Int): List<Place> { calls++; gate?.await(); return places }
        override suspend fun getPlaceById(id: String): Place? = null
    }
    @Before fun setUp() { Dispatchers.setMain(dispatcher) }
    @After fun tearDown() { Dispatchers.resetMain() }

    @Test fun selectionFiltersExploreButLeavesFeaturedShopsUnchanged() = runTest(dispatcher) {
        val repository = FakeRepository(listOf(cafe, shop))
        val vm = ExploreViewModel(GetFeaturedPlacesUseCase(repository))
        advanceUntilIdle()
        val originalFeatured = (vm.state.value as ExploreUiState.Content).featuredShops
        vm.onIntent(ExploreIntent.CategorySelected(PlaceCategory.CAFE))
        val state = vm.state.value as ExploreUiState.Content
        assertEquals(listOf(cafe), state.places)
        assertEquals(PlaceCategory.CAFE, state.selectedCategory)
        assertEquals(originalFeatured, state.featuredShops)
        assertEquals(1, repository.calls)
        vm.onIntent(ExploreIntent.CategorySelected(null))
        assertEquals(listOf(cafe, shop), (vm.state.value as ExploreUiState.Content).places)
    }

    @Test fun emptyCategoryHasActualZeroResults() = runTest(dispatcher) {
        val vm = ExploreViewModel(GetFeaturedPlacesUseCase(FakeRepository(listOf(cafe))))
        advanceUntilIdle()
        vm.onIntent(ExploreIntent.CategorySelected(PlaceCategory.RESTAURANT))
        assertTrue((vm.state.value as ExploreUiState.Content).places.isEmpty())
    }

    @Test fun categoryChosenDuringLoadingIsAppliedAfterLoad() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val repository = FakeRepository(listOf(cafe, shop)).apply { this.gate = gate }
        val vm = ExploreViewModel(GetFeaturedPlacesUseCase(repository))
        runCurrent()
        vm.onIntent(ExploreIntent.CategorySelected(PlaceCategory.LOCAL_BUSINESS))
        gate.complete(Unit)
        advanceUntilIdle()
        assertEquals(listOf(shop), (vm.state.value as ExploreUiState.Content).places)
    }

    @Test fun legacyQueryAndCategoryComposeWithoutResettingSelection() = runTest(dispatcher) {
        val vm = ExploreViewModel(GetFeaturedPlacesUseCase(FakeRepository(listOf(cafe, shop))))
        advanceUntilIdle()
        vm.onIntent(ExploreIntent.CategorySelected(PlaceCategory.CAFE))
        vm.onIntent(ExploreIntent.SearchQueryChanged("missing"))
        assertTrue((vm.state.value as ExploreUiState.Content).places.isEmpty())
        vm.onIntent(ExploreIntent.SearchQueryChanged(""))
        assertEquals(listOf(cafe), (vm.state.value as ExploreUiState.Content).places)
        assertEquals(PlaceCategory.CAFE, (vm.state.value as ExploreUiState.Content).selectedCategory)
    }
}