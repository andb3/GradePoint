package com.andb.apps.aspen.util

import kotlinx.coroutines.*

actual fun newIOThread(block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.Default).launch(block = block)
}

actual fun <T> asyncIO(block: suspend CoroutineScope.() -> T): Deferred<T> {
    return CoroutineScope(Dispatchers.Default).async(block = block)
}

actual suspend fun mainThread(block: suspend CoroutineScope.() -> Unit) {
    withContext(Dispatchers.Main, block)
}

actual suspend fun ioThread(block: suspend CoroutineScope.() -> Unit) {
    withContext(Dispatchers.Default, block)
}
