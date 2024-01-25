package com.github.snuffix.flickrapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

@Database(entities = [FlickrItemEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FlickrDatabase : RoomDatabase() {
    abstract fun flickrDao(): FlickrDao
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}