package com.andb.apps.aspen

import kotlinx.coroutines.*

actual fun currentTimeMillis(): Long = System.currentTimeMillis()

internal actual fun printThrowable(t: Throwable) {
    t.printStackTrace()
}

actual fun newIOThread(block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.IO).launch(block = block)
}

actual fun <T> asyncIO(block: suspend CoroutineScope.() -> T): Deferred<T> {
    return CoroutineScope(Dispatchers.IO).async(block = block)
}

actual suspend fun mainThread(block: suspend CoroutineScope.() -> Unit) {
    withContext(Dispatchers.Main, block)
}

actual suspend fun ioThread(block: suspend CoroutineScope.() -> Unit) {
    withContext(Dispatchers.IO, block)
}