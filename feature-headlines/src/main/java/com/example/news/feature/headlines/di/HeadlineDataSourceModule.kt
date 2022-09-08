package com.example.news.feature.headlines.di

import com.example.news.base.network.BuildConfig
import com.example.news.feature.headlines.datasource.HeadlinesApiDataSource
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HeadlineDataSourceModule {
    @Provides
    @Singleton
    fun provide(okHttpClient: OkHttpClient, gson: Gson) : HeadlinesApiDataSource {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(HeadlinesApiDataSource::class.java)
    }
}
