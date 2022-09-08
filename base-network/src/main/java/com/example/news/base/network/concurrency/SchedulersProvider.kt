package com.example.news.base.network.concurrency

import io.reactivex.Scheduler

interface SchedulersProvider {
    fun mainUiThread(): Scheduler

    fun io(): Scheduler

    fun computation(): Scheduler

    fun immediate(): Scheduler
}