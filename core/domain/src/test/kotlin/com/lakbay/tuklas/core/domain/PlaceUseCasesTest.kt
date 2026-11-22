package com.lakbay.tuklas.core.domain

import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class PlaceUseCasesTest {
    private val place = Place("sample", "Sample", PlaceCategory.CAFE, "Malolos", "Fictional")

    private class FakePlaceRepository(private val places: List<Place>) : PlaceRepository {
        var requestedLimit: Int? = null
        var requestedId: String? = null
        var failure: Exception? = null
        override suspend fun getFeaturedPlaces(limit: Int): List<Place> {
            requestedLimit = limit
            failure?.let { throw it }
            return places.take(limit)
        }
        override suspend fun getPlaceById(id: String): Place? {
            requestedId = id
            failure?.let { throw it }
            return places.find { it.id == id }
        }
    }

    @Test fun `featured forwards limit and returns repository result`() = runTest {
        val repository = FakePlaceRepository(listOf(place))
        assertEquals(listOf(place), GetFeaturedPlacesUseCase(repository)(3))
        assertEquals(3, repository.requestedLimit)
    }

    @Test fun `featured defaults to ten and preserves empty results`() = runTest {
        val repository = FakePlaceRepository(emptyList())
        assertTrue(GetFeaturedPlacesUseCase(repository)().isEmpty())
        assertEquals(10, repository.requestedLimit)
    }

    @Test fun `zero limit skips repository`() = runTest {
        val repository = FakePlaceRepository(listOf(place))
        assertTrue(GetFeaturedPlacesUseCase(repository)(0).isEmpty())
        assertNull(repository.requestedLimit)
    }

    @Test fun `negative limit fails before repository access`() = runTest {
        val repository = FakePlaceRepository(emptyList())
        try {
            GetFeaturedPlacesUseCase(repository)(-1)
            fail("Expected validation error")
        } catch (_: IllegalArgumentException) { }
        assertNull(repository.requestedLimit)
    }

    @Test fun `lookup forwards ID and returns matching place`() = runTest {
        val repository = FakePlaceRepository(listOf(place))
        assertEquals(place, GetPlaceByIdUseCase(repository)("sample"))
        assertEquals("sample", repository.requestedId)
    }

    @Test fun `missing place returns null`() = runTest {
        assertNull(GetPlaceByIdUseCase(FakePlaceRepository(emptyList()))("missing"))
    }

    @Test fun `blank ID fails before repository access`() = runTest {
        val repository = FakePlaceRepository(emptyList())
        try {
            GetPlaceByIdUseCase(repository)(" ")
            fail("Expected validation error")
        } catch (_: IllegalArgumentException) { }
        assertNull(repository.requestedId)
    }

    @Test fun `both use cases preserve repository failures`() = runTest {
        val error = IOException("offline")
        val repository = FakePlaceRepository(emptyList()).apply { failure = error }
        try {
            GetFeaturedPlacesUseCase(repository)()
            fail("Expected repository failure")
        } catch (actual: IOException) { assertSame(error, actual) }
        try {
            GetPlaceByIdUseCase(repository)("sample")
            fail("Expected repository failure")
        } catch (actual: IOException) { assertSame(error, actual) }
    }
}
