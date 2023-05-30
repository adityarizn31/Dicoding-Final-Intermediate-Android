package com.dicoding.sub1_appsstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.annotation.VisibleForTesting
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.time.DurationUnit
import kotlin.time.toTimeUnit

// Untuk menunggu sampai Livedata mendapatkan Nilai pertama
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time :Long = 2,
    timeUnit: TimeUnit = DurationUnit.SECONDS.toTimeUnit(),
    afterObserver: () -> Unit = {}
) : T {
    var data : T? = null
    val latch = CountDownLatch(1)
    val observer = object  : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)
    try {
        afterObserver.invoke()
        if (!latch.await(time, timeUnit))
            throw TimeoutException("Liva Data is Failed")
    } finally {
        this.removeObserver(observer)
    }
    @Suppress("UNCHECKED_CAST")
    return data as T
}