package app.crypto.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher

object TestDispatchers: CoroutineDispatchers {
    override val Main: CoroutineDispatcher = UnconfinedTestDispatcher()
    override val Default: CoroutineDispatcher = UnconfinedTestDispatcher()
    override val IO: CoroutineDispatcher = UnconfinedTestDispatcher()
    override val Unconfined: CoroutineDispatcher = UnconfinedTestDispatcher()
}