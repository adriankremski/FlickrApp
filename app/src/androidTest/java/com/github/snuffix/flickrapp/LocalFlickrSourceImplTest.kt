package com.github.snuffix.flickrapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.snuffix.flickrapp.data.local.FlickrDatabase
import com.github.snuffix.flickrapp.data.local.LocalFlickrSourceImpl
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class LocalFlickrSourceImplTest {

    private lateinit var flickrDatabase: FlickrDatabase
    private lateinit var localFlickrSource: LocalFlickrSourceImpl

    @Before
    fun setup() {
        flickrDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FlickrDatabase::class.java
        ).allowMainThreadQueries().build()

        val flickrDao = flickrDatabase.flickrDao()
        localFlickrSource = LocalFlickrSourceImpl(flickrDao)
    }

    @After
    fun tearDown() {
        flickrDatabase.close()
    }

    @Test
    fun getItems_shouldReturnNoItemsInitially() = runBlocking {
        // Act
        val items = localFlickrSource.getItems()

        // Assert
        assertEquals(0, items.size)
    }

    @Test
    fun replaceItemsRemoveOldItems() = runBlocking {
        // Arrange
        val itemsToInsert = listOf(flickrItem(), flickrItem(), flickrItem())

        // Act
        localFlickrSource.replaceItems(List(10) { flickrItem() })
        localFlickrSource.replaceItems(itemsToInsert)
        val retrievedItems = localFlickrSource.getItems()

        // Assert
        assertEquals(itemsToInsert.size, retrievedItems.size)
        assertEquals(itemsToInsert, retrievedItems)
    }
}

fun flickrItem() = com.github.snuffix.domain.repository.FlickrItem(
    title = randomId(),
    imageUrl = randomId(),
    description = randomId(),
    published = Date(),
    link = ""
)

private fun randomId() = UUID.randomUUID().toString()
