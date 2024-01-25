package com.github.snuffix.flickrapp.data.network

import com.github.snuffix.domain.repository.RemoteFlickrSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://api.flickr.com")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideFlickrApiService(retrofit: Retrofit): FlickrApiService =
        retrofit.create(FlickrApiService::class.java)

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideBaseOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideRemoteFlickrSource(apiService: FlickrApiService): RemoteFlickrSource =
        RemoteFlickrSourceImpl(apiService)
}