package app.crypto.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object TestDispatchers: CoroutineDispatchers {
    override val Main: CoroutineDispatcher = Dispatchers.Unconfined
    override val Default: CoroutineDispatcher = Dispatchers.Unconfined
    override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    override val Unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}