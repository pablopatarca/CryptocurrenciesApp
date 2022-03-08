package app.crypto.ui

import app.crypto.data.Repository
import app.crypto.model.AssetsListUseCase
import app.crypto.model.CryptoAsset
import app.crypto.ui.main.MainState
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
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @Mock
    private lateinit var repository: Repository
    @Mock
    private lateinit var useCase: AssetsListUseCase

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = MainViewModel(
            repository,
            useCase,
            TestDispatchers
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Fetch assets from repo when view model is initialized`() = runTest {
        // Given
        `when`(repository.fetchAssets()).thenReturn(Unit)
        // When
        viewModel.loadCryptoList()
        // Then
        verify(repository, times(2)).fetchAssets()
    }

    @Test
    fun `Emit a message when Repository throws an Exception`() = runTest {
        // Given
        val message = "Exception 💥"
        `when`(repository.fetchAssets()).thenThrow(
            NullPointerException(message)
        )
        val msgResults = mutableListOf<String>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.messages.toList(msgResults)
        }
        // When
        viewModel.loadCryptoList()
        runCurrent()
        // Then
        assert(msgResults.first() == message)
        job.cancel()
    }

    @Test
    fun `Consume the assets list produced from state flow`() = runTest {
        // Given
        val flow = flowOf(listOf(getCryptoAsset()))
        `when`(useCase.getSortedAssets()).thenReturn(flow)
        viewModel = MainViewModel(repository, useCase)
        val stateResults = mutableListOf<MainState>()

        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(stateResults)
        }
        runCurrent()

        // Then
        val cryptoList = stateResults[1].cryptoList
        assert(cryptoList.isNotEmpty())
        assert(cryptoList.first().id == "bitcoin")
        job.cancel()
    }

    @Test
    fun `Consume the assets list from the use case`() = runTest {
        // Given
        val flow = flowOf(listOf(getCryptoAsset()))
        `when`(useCase.getSortedAssets()).thenReturn(flow)
        viewModel = MainViewModel(repository, useCase)
        val stateResults = mutableListOf<MainState>()

        // When
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(stateResults)
        }
        runCurrent()

        // Then
        verify(useCase, times(2)).getSortedAssets()
        job.cancel()
    }

    private fun getCryptoAsset(): CryptoAsset {
        return CryptoAsset(
            id = "bitcoin",
            rank = 1,
            symbol = "btc",
            name = "Bitcoin",
            supply = 0.0,
            maxSupply = 0,
            marketCapUsd = 10000,
            volumeUsd24Hr = 10000,
            priceUsd = 0.0,
            changePercent24Hr = 0.0,
            vwap24Hr = 0.0,
            explorer = ""
        )
    }
}