package com.github.snuffix.flickrapp.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindFlickrRepository(repository: FlickrRepositoryImpl): FlickrRepository
}