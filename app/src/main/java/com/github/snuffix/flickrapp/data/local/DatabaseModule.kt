package com.github.snuffix.flickrapp.data.local

import android.content.Context
import androidx.room.Room
import com.github.snuffix.flickrapp.repository.LocalFlickrSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): FlickrDatabase =
        Room.databaseBuilder(
            context,
            FlickrDatabase::class.java,
            "app_database"
        ).build()

    @Provides
    @Singleton
    fun provideFlickrDao(database: FlickrDatabase): FlickrDao = database.flickrDao()

    @Provides
    @Singleton
    fun provideFlickrLocalSource(
        flickrDao: FlickrDao
    ): LocalFlickrSource = LocalFlickrSourceImpl(flickrDao)
}