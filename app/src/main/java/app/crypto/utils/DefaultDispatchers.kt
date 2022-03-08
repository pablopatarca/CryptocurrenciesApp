package app.crypto.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object DefaultDispatchers: CoroutineDispatchers {
    override val Main: CoroutineDispatcher get() = Dispatchers.Main
    override val Default: CoroutineDispatcher = Dispatchers.Default
    override val IO: CoroutineDispatcher = Dispatchers.IO
    override val Unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}