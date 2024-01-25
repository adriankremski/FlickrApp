package com.github.snuffix.flickrapp.data.network

import com.github.snuffix.flickrapp.repository.RemoteFlickrSource
import com.github.snuffix.flickrapp.repository.FlickrItem

class RemoteFlickrSourceImpl(
    private val apiService: FlickrApiService,
) : RemoteFlickrSource {
    override suspend fun getItems(): List<FlickrItem> {
        return apiService.getFlickrItems().items.map(FlickrItemDTO::toDomainModel)
    }
}

fun FlickrItemDTO.toDomainModel() = FlickrItem(
    title = title,
    imageUrl = media.url,
    description = description,
    published = published
)
