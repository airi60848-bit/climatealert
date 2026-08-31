package com.example.climatealert.di

import com.example.climatealert.BuildConfig
import com.example.climatealert.data.remote.api.RouteApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    // --- 1. OpenWeatherMap API (for Home Screen) ---
    @Provides
    @Singleton
    @Named("weather")
    fun provideWeatherApi(client: OkHttpClient): RouteApi {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RouteApi::class.java)
    }

    // --- 2.  Node.js Backend (for Routes, Forecast, Chat) ---
    @Provides
    @Singleton
    @Named("backend")
    fun provideBackendApi(client: OkHttpClient): RouteApi {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/") // Emulator: 10.0.2.2 | Real device: Use your LAN IP
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RouteApi::class.java)
    }

    // --- 3. OpenWeatherMap API Key ---
    @Provides
    @Singleton
    fun provideApiKey(): String = BuildConfig.OPEN_WEATHER_API_KEY
}