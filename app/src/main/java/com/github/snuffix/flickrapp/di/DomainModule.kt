package com.github.snuffix.flickrapp.di

import com.github.snuffix.domain.repository.FlickrRepository
import com.github.snuffix.domain.repository.FlickrRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    @Binds
    abstract fun bindFlickrRepository(repository: FlickrRepositoryImpl): FlickrRepository
}