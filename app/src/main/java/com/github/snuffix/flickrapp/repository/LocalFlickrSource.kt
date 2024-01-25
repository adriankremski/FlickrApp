package com.github.snuffix.flickrapp.repository

interface LocalFlickrSource {
    suspend fun getItems(): List<FlickrItem>
    suspend fun replaceItems(items: List<FlickrItem>)
}