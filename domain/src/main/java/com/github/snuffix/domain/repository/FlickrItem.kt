package com.github.snuffix.domain.repository

import java.util.Date

data class FlickrItem(
    val title: String,
    val link: String,
    val imageUrl: String,
    val description: String,
    val published: Date,
)
