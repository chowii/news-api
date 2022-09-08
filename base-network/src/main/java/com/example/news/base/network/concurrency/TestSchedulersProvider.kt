package com.example.news.base.network.concurrency

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler

open class TestSchedulersProvider : SchedulersProvider {
    override fun mainUiThread(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun computation(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun immediate(): Scheduler {
        return Schedulers.trampoline()
    }
}
