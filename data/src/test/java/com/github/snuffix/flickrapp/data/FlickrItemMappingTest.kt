package com.github.snuffix.flickrapp.data

import com.github.snuffix.flickrapp.data.local.FlickrItemEntity
import com.github.snuffix.flickrapp.data.local.toDatabaseModel
import com.github.snuffix.flickrapp.data.local.toDomainModel
import com.github.snuffix.flickrapp.data.network.FlickrItemDTO
import com.github.snuffix.flickrapp.data.network.MediaDTO
import com.github.snuffix.flickrapp.data.network.toDomainModel
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Date
import java.util.UUID

class FlickItemMappingTest {

    @Test
    fun `FlickrItem toDatabaseModel converts correctly`() {
        // Arrange
        val flickrItem = flickrItem()

        // Act
        val flickrItemEntity = flickrItem.toDatabaseModel()

        // Assert
        assertEquals(flickrItem.title, flickrItemEntity.title)
        assertEquals(flickrItem.imageUrl, flickrItemEntity.imageUrl)
        assertEquals(flickrItem.description, flickrItemEntity.description)
        assertEquals(flickrItem.published, flickrItemEntity.published)
    }

    @Test
    fun `FlickrItemEntity toDomainModel converts correctly`() {
        // Arrange
        val flickrItemEntity = FlickrItemEntity(
            id = "https://example.com/image.jpg",
            title = "Test Title",
            imageUrl = "https://example.com/image.jpg",
            description = "Test description",
            published = Date(),
            link = "https://example.com/image.jpg"
        )

        // Act
        val flickrItem = flickrItemEntity.toDomainModel()

        // Assert
        assertEquals(flickrItemEntity.title, flickrItem.title)
        assertEquals(flickrItemEntity.imageUrl, flickrItem.imageUrl)
        assertEquals(flickrItemEntity.description, flickrItem.description)
        assertEquals(flickrItemEntity.published, flickrItem.published)
    }

    @Test
    fun `FlickrItemDTO toDomainModel converts correctly`() {
        // Arrange
        val flickrItemDTO = FlickrItemDTO(
            title = "Test Title",
            media = MediaDTO("https://example.com/image.jpg"),
            description = "Test description",
            published = Date(),
            link = "https://example.com/image.jpg"
        )

        // Act
        val flickrItem = flickrItemDTO.toDomainModel()

        // Assert
        assertEquals(flickrItemDTO.title, flickrItem.title)
        assertEquals(flickrItemDTO.media.url, flickrItem.imageUrl)
        assertEquals(flickrItemDTO.description, flickrItem.description)
        assertEquals(flickrItemDTO.published, flickrItem.published)
    }
}
fun flickrItem() = com.github.snuffix.domain.repository.FlickrItem(
    title = randomId(),
    imageUrl = randomId(),
    description = randomId(),
    published = Date(),
    link = randomId()
)

private fun randomId() = UUID.randomUUID().toString()
