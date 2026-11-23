package com.lakbay.tuklas.feature.discover.data.repository

import com.lakbay.tuklas.feature.discover.data.dto.PlaceDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldPath
import com.lakbay.tuklas.feature.discover.domain.model.Place
import com.lakbay.tuklas.feature.discover.domain.repository.PlaceRepository
import kotlinx.coroutines.tasks.await

private const val PLACES_COLLECTION = "places"
private const val FEATURED_FIELD = "featured"

class FirestorePlaceRepository(private val firestore: FirebaseFirestore) : PlaceRepository {
    override suspend fun getFeaturedPlaces(limit: Int): List<Place> {
        require(limit >= 0) { "Limit must not be negative" }
        if (limit == 0) return emptyList()
        return firestore.collection(PLACES_COLLECTION)
            .whereEqualTo(FEATURED_FIELD, true)
            .orderBy(FieldPath.documentId())
            .limit(limit.toLong())
            .get().await().documents.map { document ->
                requireNotNull(document.toObject(PlaceDto::class.java)) {
                    "Unable to decode place ${document.id}"
                }.toDomain(document.id)
            }
    }

    override suspend fun getPlaceById(id: String): Place? {
        require(id.isNotBlank()) { "Place ID must not be blank" }
        val document = firestore.collection(PLACES_COLLECTION).document(id).get().await()
        if (!document.exists()) return null
        return requireNotNull(document.toObject(PlaceDto::class.java)) {
            "Unable to decode place ${document.id}"
        }.toDomain(document.id)
    }
}
