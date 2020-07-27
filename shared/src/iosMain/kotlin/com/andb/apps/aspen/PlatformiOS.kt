package com.andb.apps.aspen

import kotlinx.coroutines.*
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual fun currentTimeMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()

internal actual fun printThrowable(t: Throwable) {
    t.printStackTrace()
}

actual val ioDispatcher: CoroutineDispatcher = Dispatchers.Default