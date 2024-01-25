package com.github.snuffix.flickrapp.repository

interface RemoteFlickrSource {
    suspend fun getItems(): List<FlickrItem>
}