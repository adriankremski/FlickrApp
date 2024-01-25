package com.github.snuffix.flickrapp.repository

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.coroutines.runSuspendCatching
import com.github.michaelbull.result.mapError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FlickrRepositoryImpl @Inject constructor(
    private val remoteFlickrSource: RemoteFlickrSource,
    private val localFlickrSource: LocalFlickrSource
) : FlickrRepository {
    override fun getFlickrItems(): Flow<Result<List<FlickrItem>, RepositoryError>> = flow {
        val cachedItems = localFlickrSource.getItems()
        if (cachedItems.isNotEmpty()) {
            emit(Ok(cachedItems))
        }

        runSuspendCatching {
            remoteFlickrSource.getItems()
        }.andThen { items ->
            localFlickrSource.replaceItems(items)
            Ok(items)
        }.mapError {
            RepositoryError.GetFlickrItemsError(cachedItems)
        }.apply {
            emit(this)
        }
    }.flowOn(Dispatchers.IO)
}