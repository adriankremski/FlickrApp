package com.github.snuffix.flickrapp.data.network

import androidx.annotation.Keep
import com.squareup.moshi.Json
import java.util.Date

@Keep
class FlickrItemDTO(
    @Json(name = "title") val title: String,
    @Json(name = "media") val media: MediaDTO,
    @Json(name = "description") val description: String,
    @Json(name = "published") val published: Date,
)

@Keep
class MediaDTO(
    @Json(name = "m") val url: String,
)
