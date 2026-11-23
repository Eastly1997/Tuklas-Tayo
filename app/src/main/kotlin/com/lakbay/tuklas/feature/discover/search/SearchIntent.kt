package com.lakbay.tuklas.feature.discover.search

sealed interface SearchIntent {
    data class QueryChanged(val query: String) : SearchIntent
    data object Retry : SearchIntent
}