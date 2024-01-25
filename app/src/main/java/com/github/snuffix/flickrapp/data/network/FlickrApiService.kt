package com.github.snuffix.flickrapp.data.network

import androidx.annotation.Keep
import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApiService {
    @GET("services/feeds/photos_public.gne")
    suspend fun getFlickrItems(
        @Query("format") format: String = "json",
        @Query("tags") tags: List<String> = listOf("cat"),
        @Query("nojsoncallback") noJsonCallback: Int = 1
    ): GetFlickrItemsResponse
}

@Keep
class GetFlickrItemsResponse(
    @Json(name = "items") val items: List<FlickrItemDTO>
)