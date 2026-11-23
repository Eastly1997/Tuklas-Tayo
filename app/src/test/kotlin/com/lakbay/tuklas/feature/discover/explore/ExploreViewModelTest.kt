package com.lakbay.tuklas.feature.discover.explore

import com.lakbay.tuklas.domain.usecase.GetFeaturedPlacesUseCase
import com.lakbay.tuklas.domain.model.Place
import com.lakbay.tuklas.domain.model.PlaceCategory
import com.lakbay.tuklas.domain.repository.PlaceRepository
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class ExploreViewModelTest {
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

    @Test fun announcementSurvivesSearchAndClearingThroughViewModel() = runTest(dispatcher) {
        val repository = FakeRepository().apply { result = listOf(sample) }
        val announcement = AnnouncementUiModel("welcome", "Welcome", "Local favorites")
        val viewModel = ExploreViewModel(GetFeaturedPlacesUseCase(repository), initialAnnouncement = announcement)
        advanceUntilIdle()
        assertEquals(announcement, (viewModel.state.value as ExploreUiState.Content).visibleAnnouncement)
        viewModel.onIntent(ExploreIntent.SearchQueryChanged("sample"))
        assertNull((viewModel.state.value as ExploreUiState.Content).visibleAnnouncement)
        viewModel.onIntent(ExploreIntent.SearchQueryChanged(""))
        assertEquals(announcement, (viewModel.state.value as ExploreUiState.Content).visibleAnnouncement)
        assertEquals(1, repository.calls)
    }
    @Test fun `search hides featured and clear restores without fetching again`() = runTest(dispatcher) {
        val other = sample.copy(id = "other", name = "Other", address = "Manila")
        val repository = FakeRepository().apply { result = listOf(sample, other) }
        val viewModel = ExploreViewModel(GetFeaturedPlacesUseCase(repository))
        advanceUntilIdle()
        viewModel.onIntent(ExploreIntent.SearchQueryChanged("  mALoLos "))
        val searched = viewModel.state.value as ExploreUiState.Content
        assertEquals(listOf(sample), searched.places)
        assertTrue(searched.featuredShops.isEmpty())
        viewModel.onIntent(ExploreIntent.SearchQueryChanged("unknown"))
        assertTrue((viewModel.state.value as ExploreUiState.Content).places.isEmpty())
        viewModel.onIntent(ExploreIntent.SearchQueryChanged("  "))
        val restored = viewModel.state.value as ExploreUiState.Content
        assertEquals(listOf(sample, other), restored.places)
        assertEquals(2, restored.featuredShops.size)
        assertEquals(1, repository.calls)
    }

    @Test fun `query arriving during load is applied to loaded content`() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val repository = FakeRepository().apply { this.gate = gate; result = listOf(sample) }
        val viewModel = ExploreViewModel(GetFeaturedPlacesUseCase(repository))
        runCurrent()
        viewModel.onIntent(ExploreIntent.SearchQueryChanged("no match"))
        gate.complete(Unit)
        advanceUntilIdle()
        val content = viewModel.state.value as ExploreUiState.Content
        assertEquals("no match", content.searchQuery)
        assertTrue(content.places.isEmpty())
        assertTrue(content.featuredShops.isEmpty())
    }

    @Test fun `initial load transitions from loading to content`() = runTest(dispatcher) {
        val repository = FakeRepository().apply { result = listOf(sample) }
        val viewModel = ExploreViewModel(GetFeaturedPlacesUseCase(repository))
        assertEquals(ExploreUiState.Loading, viewModel.state.value)
        advanceUntilIdle()
        assertEquals(ExploreUiState.Content(listOf(sample)), viewModel.state.value)
    }

    @Test fun `empty result is content rather than error`() = runTest(dispatcher) {
        val viewModel = ExploreViewModel(GetFeaturedPlacesUseCase(FakeRepository()))
        advanceUntilIdle()
        assertEquals(ExploreUiState.Content(emptyList()), viewModel.state.value)
    }

    @Test fun `failed load can retry through loading to content`() = runTest(dispatcher) {
        val repository = FakeRepository().apply { failure = IOException("offline") }
        val viewModel = ExploreViewModel(GetFeaturedPlacesUseCase(repository))
        val states = mutableListOf<ExploreUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect { states += it } }
        advanceUntilIdle()
        assertEquals(ExploreUiState.Error, viewModel.state.value)
        repository.failure = null
        repository.result = listOf(sample)
        viewModel.onIntent(ExploreIntent.Retry)
        advanceUntilIdle()
        assertEquals(listOf(ExploreUiState.Loading, ExploreUiState.Error,
            ExploreUiState.Loading, ExploreUiState.Content(listOf(sample))), states)
        assertEquals(2, repository.calls)
    }

    @Test fun `retry during an active request does not create duplicate loads`() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val repository = FakeRepository().apply { this.gate = gate }
        val viewModel = ExploreViewModel(GetFeaturedPlacesUseCase(repository))
        runCurrent()
        repeat(3) { viewModel.onIntent(ExploreIntent.Retry) }
        assertEquals(1, repository.calls)
        gate.complete(Unit)
        advanceUntilIdle()
        assertEquals(ExploreUiState.Content(emptyList()), viewModel.state.value)
    }

    @Test fun `cancellation is not presented as a loading error`() = runTest(dispatcher) {
        val repository = FakeRepository().apply { failure = CancellationException("cancelled") }
        val viewModel = ExploreViewModel(GetFeaturedPlacesUseCase(repository))
        advanceUntilIdle()
        assertEquals(ExploreUiState.Loading, viewModel.state.value)
    }
}
