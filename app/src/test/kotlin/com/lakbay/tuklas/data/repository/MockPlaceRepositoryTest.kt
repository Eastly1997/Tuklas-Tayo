package com.lakbay.tuklas.data.repository

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class MockPlaceRepositoryTest {
    @Test fun `default repository contains no fictional businesses`() = runTest {
        assertTrue(MockPlaceRepository().getFeaturedPlaces(10).isEmpty())
    }
    @Test fun `fixtures obey limits and support lookup`() = runTest {
        val repository = MockPlaceRepository(samplePlaces)
        val places = repository.getFeaturedPlaces(10)
        assertEquals(3, places.size)
        assertEquals(1, repository.getFeaturedPlaces(1).size)
        assertTrue(repository.getFeaturedPlaces(0).isEmpty())
        assertEquals(places.first(), repository.getPlaceById(places.first().id))
        assertNull(repository.getPlaceById("missing"))
        assertEquals(places.size, places.map { it.id }.distinct().size)
    }
}
