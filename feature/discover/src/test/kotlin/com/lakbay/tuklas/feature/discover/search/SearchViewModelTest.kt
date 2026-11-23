package com.lakbay.tuklas.feature.discover.search

import androidx.lifecycle.SavedStateHandle
import com.lakbay.tuklas.feature.discover.domain.model.*
import com.lakbay.tuklas.feature.discover.domain.repository.*
import com.lakbay.tuklas.feature.discover.domain.usecase.*
import com.lakbay.tuklas.feature.discover.presentation.search.SearchIntent
import com.lakbay.tuklas.feature.discover.presentation.search.SearchUiState
import com.lakbay.tuklas.feature.discover.presentation.search.SearchViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val cafe = Place("cafe", "Sample Coffee", PlaceCategory.CAFE, "Malolos", "Development sample")
    private val shop = Place("shop", "Sample Market", PlaceCategory.LOCAL_BUSINESS, "Baliwag", "")
    private class FakeRepository : PlaceRepository {
        var calls = 0
        var result: List<Place> = emptyList()
        var failure: Exception? = null
        var gate: CompletableDeferred<Unit>? = null
        override suspend fun getFeaturedPlaces(limit: Int): List<Place> {
            calls++
            gate?.await()
            failure?.let { throw it }
            return result
        }
        override suspend fun getPlaceById(id: String): Place? = null
    }
    @Before fun setUp() { Dispatchers.setMain(dispatcher) }
    @After fun tearDown() { Dispatchers.resetMain() }

    @Test fun emptyQueryLoadsDataButShowsNoBrowseResults() = runTest(dispatcher) {
        val repository = FakeRepository().apply { result = listOf(cafe, shop) }
        val vm = SearchViewModel(GetFeaturedPlacesUseCase(repository), SavedStateHandle())
        advanceUntilIdle()
        assertEquals(SearchUiState.Content("", emptyList()), vm.state.value)
        assertEquals(1, repository.calls)
    }

    @Test fun existingNameCategoryAndAddressMatchingIsReusedWithoutReloading() = runTest(dispatcher) {
        val repository = FakeRepository().apply { result = listOf(cafe, shop) }
        val vm = SearchViewModel(GetFeaturedPlacesUseCase(repository), SavedStateHandle())
        advanceUntilIdle()
        for (query in listOf(" coffee ", "CAFÉ", "cafe", "mALoLoS")) {
            vm.onIntent(SearchIntent.QueryChanged(query))
            assertEquals(listOf(cafe), (vm.state.value as SearchUiState.Content).places)
        }
        vm.onIntent(SearchIntent.QueryChanged("local business"))
        assertEquals(listOf(shop), (vm.state.value as SearchUiState.Content).places)
        vm.onIntent(SearchIntent.QueryChanged("unknown"))
        assertTrue((vm.state.value as SearchUiState.Content).places.isEmpty())
        assertEquals(1, repository.calls)
    }

    @Test fun clearingRestoresPromptStateAndPersistsTheQuery() = runTest(dispatcher) {
        val handle = SavedStateHandle()
        val repository = FakeRepository().apply { result = listOf(cafe) }
        val vm = SearchViewModel(GetFeaturedPlacesUseCase(repository), handle)
        advanceUntilIdle()
        vm.onIntent(SearchIntent.QueryChanged("coffee"))
        assertEquals("coffee", handle.get<String>("search_query"))
        vm.onIntent(SearchIntent.QueryChanged(""))
        assertEquals(SearchUiState.Content("", emptyList()), vm.state.value)
        vm.onIntent(SearchIntent.QueryChanged("  "))
        assertTrue((vm.state.value as SearchUiState.Content).places.isEmpty())
    }

    @Test fun savedQueryIsRestoredAndResultsAreRebuilt() = runTest(dispatcher) {
        val repository = FakeRepository().apply { result = listOf(cafe) }
        val vm = SearchViewModel(
            GetFeaturedPlacesUseCase(repository),
            SavedStateHandle(mapOf("search_query" to "coffee"))
        )
        advanceUntilIdle()
        assertEquals(SearchUiState.Content("coffee", listOf(cafe)), vm.state.value)
    }

    @Test fun latestInputDuringLoadingIsAppliedAndDuplicateRetriesAreIgnored() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val repository = FakeRepository().apply { this.gate = gate; result = listOf(cafe, shop) }
        val vm = SearchViewModel(GetFeaturedPlacesUseCase(repository), SavedStateHandle())
        runCurrent()
        vm.onIntent(SearchIntent.QueryChanged("Baliwag"))
        repeat(3) { vm.onIntent(SearchIntent.Retry) }
        assertEquals(1, repository.calls)
        gate.complete(Unit)
        advanceUntilIdle()
        assertEquals(SearchUiState.Content("Baliwag", listOf(shop)), vm.state.value)
    }

    @Test fun failureRetryPreservesInput() = runTest(dispatcher) {
        val repository = FakeRepository().apply { failure = IOException("offline") }
        val vm = SearchViewModel(GetFeaturedPlacesUseCase(repository), SavedStateHandle())
        advanceUntilIdle()
        vm.onIntent(SearchIntent.QueryChanged("coffee"))
        assertEquals(SearchUiState.Error("coffee"), vm.state.value)
        repository.failure = null
        repository.result = listOf(cafe)
        vm.onIntent(SearchIntent.Retry)
        advanceUntilIdle()
        assertEquals(SearchUiState.Content("coffee", listOf(cafe)), vm.state.value)
    }

    @Test fun cancellationDoesNotBecomeAnError() = runTest(dispatcher) {
        val repository = FakeRepository().apply { failure = CancellationException("cancelled") }
        val vm = SearchViewModel(GetFeaturedPlacesUseCase(repository), SavedStateHandle())
        advanceUntilIdle()
        assertTrue(vm.state.value is SearchUiState.Loading)
    }
}