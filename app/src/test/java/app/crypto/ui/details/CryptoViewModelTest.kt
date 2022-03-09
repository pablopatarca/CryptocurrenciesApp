package app.crypto.ui.details

import androidx.lifecycle.SavedStateHandle
import app.crypto.data.AssetEntity
import app.crypto.data.PriceEntity
import app.crypto.data.Repository
import app.crypto.model.CryptoAsset
import app.crypto.utils.TestDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
/**
 * Tests running under StandardTestDispatcher and UnconfinedTestDispatcher.
 * for more info see https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/kotlinx.coroutines.test/-standard-test-dispatcher.html
 * https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/kotlinx.coroutines.test/-unconfined-test-dispatcher.html
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CryptoViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var repository: Repository
    private var savedStateHandle = SavedStateHandle().apply { set("id", "bitcoin") }
    private lateinit var viewModel: CryptoViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CryptoViewModel(
            repository,
            savedStateHandle,
            TestDispatchers
        )
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Emit a message when Repository throws an Exception`() = runTest {
        // Given
        val message = "Exception 💥"
        `when`(repository.fetchHistory("bitcoin")).thenThrow(
            NullPointerException(message)
        )
        val msgResults = mutableListOf<String>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.messages.toList(msgResults)
        }
        // When
        viewModel.fetchInfo("bitcoin")
        runCurrent()
        // Then
        assert(msgResults.first() == message)
        job.cancel()
    }

    @Test
    fun `Consume CryptoAssets when ViewModel is initialized`() = runTest {
        // Given
        `when`(repository.getAssetById("bitcoin")).thenReturn(getFakeAsset())
        val results = mutableListOf<CryptoAsset?>()
        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(results)
        }
        runCurrent()
        // Then
        assert(results.first() == null)
        assert(results[1]?.id == "bitcoin")
        job.cancel()
    }

    @Test
    fun `Get CryptoAssets by id from the repo`() = runTest {
        // Given
        `when`(repository.getAssetById("bitcoin")).thenReturn(getFakeAsset())
        val results = mutableListOf<CryptoAsset?>()
        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(results)
        }
        runCurrent()
        // Then
        verify(repository).getAssetById("bitcoin")
        job.cancel()
    }

    @Test
    fun `Emit the history after retrieving the CryptoAsset`() = runTest {
        // Given
        `when`(repository.getAssetById("bitcoin")).thenReturn(getFakeAsset())
        val f = flowOf(listOf(getFakePrice()))
        `when`(repository.getHistory("bitcoin")).thenReturn(f)
        val results = mutableListOf<List<PriceEntity>>()
        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.history.toList(results)
        }
        runCurrent()
        // Then
        println(results)
        assert(results.first().isEmpty())
        assert(results[1][0].quoteId == "bitcoin")
        job.cancel()
    }

    @Test
    fun `Fetch history from repository when CryptoAsset is loaded`() = runTest {
        // Given
        `when`(repository.getAssetById("bitcoin")).thenReturn(getFakeAsset())
        val f = flowOf(listOf(getFakePrice()))
        `when`(repository.getHistory("bitcoin")).thenReturn(f)
        val results = mutableListOf<List<PriceEntity>>()
        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.history.toList(results)
        }
        runCurrent()
        // Then
        verify(repository).fetchHistory("bitcoin")
        job.cancel()
    }

    @Test
    fun `Consume asset price history produced from the repo`() = runTest {
        // Given
        `when`(repository.getAssetById("bitcoin")).thenReturn(getFakeAsset())
        val f = flowOf(listOf(getFakePrice()))
        `when`(repository.getHistory("bitcoin")).thenReturn(f)
        val results = mutableListOf<List<PriceEntity>>()
        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.history.toList(results)
        }
        runCurrent()
        // Then
        verify(repository).getHistory("bitcoin")
        job.cancel()
    }

    private fun getFakeAsset(): AssetEntity {
        return AssetEntity(
            id = "bitcoin",
            rank = 1,
            symbol = "BTC",
            name = "bitcoin",
            supply = 0.0,
            maxSupply = 0,
            marketCapUsd = 0,
            volumeUsd24Hr = 0,
            priceUsd = 0.0,
            changePercent24Hr = 0.0,
            vwap24Hr = 0.0,
            explorer = "",
        )
    }
    private fun getFakePrice(): PriceEntity {
        return PriceEntity(
            time = 1L,
            quoteId = "bitcoin",
            priceUsd = 1.0
        )
    }
}