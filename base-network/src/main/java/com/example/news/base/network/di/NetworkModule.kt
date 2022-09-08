package com.example.news.base.network.di

import com.example.news.base.network.BuildConfig
import com.example.news.base.network.connection.ConnectionManager
import com.example.news.base.network.interceptor.ApiKeyInterceptor
import com.example.news.base.network.interceptor.RequestInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        connectionManager: ConnectionManager,
    ): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
            .addInterceptor(RequestInterceptor(connectionManager))
            .addInterceptor(apiKeyInterceptor)

        if (BuildConfig.DEBUG) {
            val httpLogging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpBuilder.addInterceptor(httpLogging)
        }
        return okHttpBuilder.build()
    }

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): ApiKeyInterceptor = ApiKeyInterceptor(BuildConfig.API_KEY)
}
