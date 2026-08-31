package com.example.climatealert.di

import com.example.climatealert.data.repository.*
import com.example.climatealert.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindWeather(impl: WeatherRepositoryImpl): WeatherRepository
    @Binds @Singleton abstract fun bindRoute(impl: RouteRepositoryImpl): RouteRepository
    @Binds @Singleton abstract fun bindAdvisory(impl: AdvisoryRepositoryImpl): AdvisoryRepository
    @Binds @Singleton abstract fun bindAdvisor(impl: AdvisorRepositoryImpl): AdvisorRepository
}