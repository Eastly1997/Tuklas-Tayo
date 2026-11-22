package com.lakbay.tuklas.feature.discover.presentation

import com.lakbay.tuklas.core.domain.GetFeaturedPlacesUseCase
import com.lakbay.tuklas.core.domain.Place
import com.lakbay.tuklas.core.domain.PlaceCategory
import com.lakbay.tuklas.core.domain.PlaceRepository
import com.lakbay.tuklas.feature.discover.presentation.contract.DiscoverIntent
import com.lakbay.tuklas.feature.discover.presentation.contract.DiscoverUiState
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoverViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val sample = Place("sample", "Sample", PlaceCategory.CAFE, "Malolos", "Fictional")

    @Before fun setUp() { Dispatchers.setMain(dispatcher) }
    @After fun tearDown() { Dispatchers.resetMain() }

    private class FakeRepository : PlaceRepository {
        var result: List<Place> = emptyList()
        var failure: Exception? = null
        var gate: CompletableDeferred<Unit>? = null
        var calls = 0
        override suspend fun getFeaturedPlaces(limit: Int): List<Place> {
            calls++
            gate?.await()
            failure?.let { throw it }
            return result
        }
        override suspend fun getPlaceById(id: String): Place? = null
    }

    @Test fun `search hides featured and clear restores without fetching again`() = runTest(dispatcher) {
        val other = sample.copy(id = "other", name = "Other", address = "Manila")
        val repository = FakeRepository().apply { result = listOf(sample, other) }
        val viewModel = DiscoverViewModel(GetFeaturedPlacesUseCase(repository))
        advanceUntilIdle()
        viewModel.onIntent(DiscoverIntent.SearchQueryChanged("  mALoLos "))
        val searched = viewModel.state.value as DiscoverUiState.Content
        assertEquals(listOf(sample), searched.places)
        assertTrue(searched.featuredShops.isEmpty())
        viewModel.onIntent(DiscoverIntent.SearchQueryChanged("unknown"))
        assertTrue((viewModel.state.value as DiscoverUiState.Content).places.isEmpty())
        viewModel.onIntent(DiscoverIntent.SearchQueryChanged("  "))
        val restored = viewModel.state.value as DiscoverUiState.Content
        assertEquals(listOf(sample, other), restored.places)
        assertEquals(2, restored.featuredShops.size)
        assertEquals(1, repository.calls)
    }

    @Test fun `query arriving during load is applied to loaded content`() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val repository = FakeRepository().apply { this.gate = gate; result = listOf(sample) }
        val viewModel = DiscoverViewModel(GetFeaturedPlacesUseCase(repository))
        runCurrent()
        viewModel.onIntent(DiscoverIntent.SearchQueryChanged("no match"))
        gate.complete(Unit)
        advanceUntilIdle()
        val content = viewModel.state.value as DiscoverUiState.Content
        assertEquals("no match", content.searchQuery)
        assertTrue(content.places.isEmpty())
        assertTrue(content.featuredShops.isEmpty())
    }

    @Test fun `initial load transitions from loading to content`() = runTest(dispatcher) {
        val repository = FakeRepository().apply { result = listOf(sample) }
        val viewModel = DiscoverViewModel(GetFeaturedPlacesUseCase(repository))
        assertEquals(DiscoverUiState.Loading, viewModel.state.value)
        advanceUntilIdle()
        assertEquals(DiscoverUiState.Content(listOf(sample)), viewModel.state.value)
    }

    @Test fun `empty result is content rather than error`() = runTest(dispatcher) {
        val viewModel = DiscoverViewModel(GetFeaturedPlacesUseCase(FakeRepository()))
        advanceUntilIdle()
        assertEquals(DiscoverUiState.Content(emptyList()), viewModel.state.value)
    }

    @Test fun `failed load can retry through loading to content`() = runTest(dispatcher) {
        val repository = FakeRepository().apply { failure = IOException("offline") }
        val viewModel = DiscoverViewModel(GetFeaturedPlacesUseCase(repository))
        val states = mutableListOf<DiscoverUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect { states += it } }
        advanceUntilIdle()
        assertEquals(DiscoverUiState.Error, viewModel.state.value)
        repository.failure = null
        repository.result = listOf(sample)
        viewModel.onIntent(DiscoverIntent.Retry)
        advanceUntilIdle()
        assertEquals(listOf(DiscoverUiState.Loading, DiscoverUiState.Error,
            DiscoverUiState.Loading, DiscoverUiState.Content(listOf(sample))), states)
        assertEquals(2, repository.calls)
    }

    @Test fun `retry during an active request does not create duplicate loads`() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val repository = FakeRepository().apply { this.gate = gate }
        val viewModel = DiscoverViewModel(GetFeaturedPlacesUseCase(repository))
        runCurrent()
        repeat(3) { viewModel.onIntent(DiscoverIntent.Retry) }
        assertEquals(1, repository.calls)
        gate.complete(Unit)
        advanceUntilIdle()
        assertEquals(DiscoverUiState.Content(emptyList()), viewModel.state.value)
    }

    @Test fun `cancellation is not presented as a loading error`() = runTest(dispatcher) {
        val repository = FakeRepository().apply { failure = CancellationException("cancelled") }
        val viewModel = DiscoverViewModel(GetFeaturedPlacesUseCase(repository))
        advanceUntilIdle()
        assertEquals(DiscoverUiState.Loading, viewModel.state.value)
    }
}
