package com.github.snuffix.flickrapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "flickr_items")
data class FlickrItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val imageUrl: String,
    val description: String,
    val published: Date,
)
