package com.example.news.base.network.di

import com.example.news.base.network.concurrency.AndroidSchedulers
import com.example.news.base.network.concurrency.SchedulersProvider

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConcurrencyModule {

    @Singleton
    @Provides
    fun provideSchedulersProvider(): SchedulersProvider = AndroidSchedulers()
}