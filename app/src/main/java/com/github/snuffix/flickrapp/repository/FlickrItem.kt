package com.github.snuffix.flickrapp.repository

import java.util.Date

data class FlickrItem(
    val title: String,
    val imageUrl: String,
    val description: String,
    val published: Date,
)
