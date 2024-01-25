package com.github.snuffix.flickrapp.data.local

import com.github.snuffix.flickrapp.repository.LocalFlickrSource
import com.github.snuffix.flickrapp.repository.FlickrItem

class LocalFlickrSourceImpl(
    private val dao: FlickrDao,
) : LocalFlickrSource {
    override suspend fun getItems(): List<FlickrItem> {
        return dao.getAll().map(FlickrItemEntity::toDomainModel)
    }

    override suspend fun replaceItems(items: List<FlickrItem>) {
        dao.deleteAll()
        dao.insertAll(items.map(FlickrItem::toDatabaseModel))
    }
}

fun FlickrItem.toDatabaseModel() = FlickrItemEntity(
    id = imageUrl,
    title = title,
    imageUrl = imageUrl,
    description = description,
    published = published
)

fun FlickrItemEntity.toDomainModel() = FlickrItem(
    title = title,
    imageUrl = imageUrl,
    description = description,
    published = published,
)
