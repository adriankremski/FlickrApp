package com.github.snuffix.flickrapp.data.network

import com.github.snuffix.domain.repository.FlickrItem
import com.github.snuffix.domain.repository.RemoteFlickrSource

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
    published = published,
    link = link
)
