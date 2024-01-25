package com.github.snuffix.domain.repository

interface RemoteFlickrSource {
    suspend fun getItems(): List<FlickrItem>
}