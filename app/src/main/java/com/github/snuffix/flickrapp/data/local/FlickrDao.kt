package com.github.snuffix.flickrapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FlickrDao {
    @Query("SELECT * FROM flickr_items ORDER BY published DESC")
    fun getAll(): List<FlickrItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<FlickrItemEntity>)

    @Query("DELETE FROM flickr_items")
    fun deleteAll()
}
