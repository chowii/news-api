package com.example.news.base.network.util

import io.reactivex.Single
import timber.log.Timber

val <V> Result<V>.value: V?
    get() = getOrNull()

fun <V : Any> Single<V>.toResult(): Single<Result<V>> = map { Result.success(it) }
    .onErrorResumeNext {
        Timber.w(it)
        Single.just(Result.failure(it))
    }