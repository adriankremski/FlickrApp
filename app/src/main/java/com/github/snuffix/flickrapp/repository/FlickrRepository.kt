package com.github.snuffix.flickrapp.repository

import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow

interface FlickrRepository {
    fun getFlickrItems(): Flow<Result<List<FlickrItem>, RepositoryError>>
}

sealed class RepositoryError {
    data class GetFlickrItemsError(val cachedItems: List<FlickrItem>): RepositoryError()
}