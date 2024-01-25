package com.github.snuffix.domain.repository

interface LocalFlickrSource {
    suspend fun getItems(): List<FlickrItem>
    suspend fun replaceItems(items: List<FlickrItem>)
}