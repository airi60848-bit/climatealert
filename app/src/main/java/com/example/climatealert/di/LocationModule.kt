package com.example.climatealert.di

import android.content.Context
import com.example.climatealert.data.remote.datasource.LocationDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    
    @Provides
    @Singleton
    fun provideLocationDataSource(@ApplicationContext context: Context): LocationDataSource {
        return LocationDataSource(context)
    }
}