package app.crypto.model

import app.crypto.data.AssetEntity
import app.crypto.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AssetsListUseCaseTest {

    @Mock
    private lateinit var repository: Repository
    // Class Under the Test
    private lateinit var useCase: AssetsListUseCaseImpl

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        useCase = AssetsListUseCaseImpl(
            repository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Transform and Sort items when a flow of Assets`() = runTest {
        // Given
        val flow = flowOf(getListAssets())
        `when`(repository.getAssets()).thenReturn(flow)
        // When
        val res = useCase.getSortedAssets().first()
        // Then
        assert(res.size == 3)
        assert(res[0].id == "dogcoin")
        assert(res[1].id == "bitcoin")
        assert(res[2].id == "letcoin")
    }

    private fun getListAssets(): List<AssetEntity> {
        return listOf(
            AssetEntity(
                id = "bitcoin",
                rank = 2,
                symbol = "btc",
                name = "Bitcoin",
                supply = 0.0,
                maxSupply = 0,
                marketCapUsd = 10000,
                volumeUsd24Hr = 10000,
                priceUsd = 0.0,
                changePercent24Hr = 0.0,
                vwap24Hr = 0.0,
                explorer = "",
            ),
            AssetEntity(
                id = "dogcoin",
                rank = 1,
                symbol = "dgc",
                name = "DogCoin",
                supply = 0.0,
                maxSupply = 0,
                marketCapUsd = 10000,
                volumeUsd24Hr = 10000,
                priceUsd = 0.0,
                changePercent24Hr = 0.0,
                vwap24Hr = 0.0,
                explorer = "",
            ),
            AssetEntity(
                id = "letcoin",
                rank = 3,
                symbol = "ltc",
                name = "Letcoin",
                supply = 0.0,
                maxSupply = 0,
                marketCapUsd = 10000,
                volumeUsd24Hr = 10000,
                priceUsd = 0.0,
                changePercent24Hr = 0.0,
                vwap24Hr = 0.0,
                explorer = "",
            )
        )
    }
}