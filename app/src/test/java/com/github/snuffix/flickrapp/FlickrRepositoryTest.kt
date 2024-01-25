package com.github.snuffix.flickrapp

import app.cash.turbine.test
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.snuffix.flickrapp.repository.FlickrItem
import com.github.snuffix.flickrapp.repository.FlickrRepositoryImpl
import com.github.snuffix.flickrapp.repository.LocalFlickrSource
import com.github.snuffix.flickrapp.repository.RemoteFlickrSource
import com.github.snuffix.flickrapp.repository.RepositoryError
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import java.io.IOException
import java.util.Date
import java.util.UUID
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class FlickrRepositoryImplTest {

    private val remoteFlickrSource: RemoteFlickrSource = mock()
    private val localFlickrSource: LocalFlickrSource = mock()
    private val flickrRepository: FlickrRepositoryImpl = FlickrRepositoryImpl(
        remoteFlickrSource, localFlickrSource
    )

    @Test
    fun `getFlickrItems should emit cached items and then error if it occurs`() =
        runBlocking {
            // Arrange
            val cachedItems = listOf(flickrItem())
            val error = IOException("Network error")

            `when`(localFlickrSource.getItems()).thenReturn(cachedItems)
            `when`(remoteFlickrSource.getItems()).thenAnswer { throw error }

            // Act
            val resultFlow = flickrRepository.getFlickrItems()

            // Assert
            resultFlow.test {
                assertEquals(Ok(cachedItems), awaitItem())
                assertEquals(Err(RepositoryError.GetFlickrItemsError(cachedItems)), awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `getFlickrItems should emit items from local source and then replaceItems in local source if remote source succeeds`() =
        runBlocking {
            // Arrange
            val cachedItems = listOf(flickrItem())
            val newItems = listOf(flickrItem())
            val successResult = Ok(newItems)

            `when`(localFlickrSource.getItems()).thenReturn(cachedItems)
            `when`(remoteFlickrSource.getItems()).thenReturn(newItems)

            // Act
            val resultFlow = flickrRepository.getFlickrItems()

            // Assert
            resultFlow.test {
                assertEquals(Ok(cachedItems), awaitItem())
                with(awaitItem()) {
                    assertEquals(successResult, this)
                    verify(localFlickrSource).replaceItems(component1()!!)
                }
                awaitComplete()
            }
        }
}

fun flickrItem() = FlickrItem(
    title = randomId(),
    imageUrl = randomId(),
    description = randomId(),
    published = Date()
)

private fun randomId() = UUID.randomUUID().toString()