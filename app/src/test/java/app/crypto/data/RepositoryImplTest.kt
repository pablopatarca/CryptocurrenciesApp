package app.crypto.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.math.BigDecimal

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryImplTest {

    @Mock
    private lateinit var api: CryptoAPI
    @Mock
    private lateinit var dao: CryptoDao
    // Class Under Test
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        repository = RepositoryImpl(
            api = api,
            dao = dao
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Consume Assets when datasource produce ONE item`() = runTest {
        // Given
        val f = flowOf(listOf(getFakeAssetEntity()))
        `when`(dao.getAssets()).thenReturn(f)
        // When
        val flowResult = repository.getAssets()
        // Then
        val data = flowResult.first()
        assert(data.isNotEmpty())
        assert(data[0].id == "bitcoin")
    }

    @Test
    fun `Consume Assets from DB once`() = runTest {
        // Given
        val f = flowOf<List<AssetEntity>>(listOf())
        `when`(dao.getAssets()).thenReturn(f)
        // When
        repository.getAssets()
        // Then
        verify(dao, times(1)).getAssets()
    }

    @Test
    fun `Fetch Assets from API and save in DAO`() = runTest {
        // Given
        val apiResult = Response.success(
            AssetsListDTO(listOf(getAssetDTO()))
        )
        `when`(api.getAssets()).thenReturn(apiResult)

        // When
        repository.fetchAssets()
        // Then
        verify(api, times(1)).getAssets()
        verify(dao, times(1)).insert(anyList())
    }

    @Test(expected = NetworkException::class)
    fun `Throw Exception when API request returns an Error`() = runTest {
        // Given
        val errorResponse = Response.error<AssetsListDTO>(
            404,
            "Unknown Network Error 🙊"
                .toResponseBody("text/plain".toMediaTypeOrNull())
        )
        `when`(api.getAssets()).thenReturn(errorResponse)

        // When
        repository.fetchAssets()
    }

    @Test
    fun `Return an Asset by Id`() = runTest {
        // Given
        `when`(dao.getAssetById("bitcoin")).thenReturn(getFakeAssetEntity())
        // When
        val asset = repository.getAssetById("bitcoin")
        // Then
        assert(asset != null)
        assert(asset?.id == "bitcoin")
    }

    @Test
    fun `Get an Asset by Id from Dao`() = runTest {
        // Given
        `when`(dao.getAssetById("bitcoin")).thenReturn(getFakeAssetEntity())
        // When
        repository.getAssetById("bitcoin")
        // Then
        verify(dao).getAssetById("bitcoin")
    }

    @Test
    fun `Get asset price history`() = runTest {
        // Given
        val f = flowOf(
            listOf(PriceEntity(0,"bitcoin",1.0))
        )
        `when`(dao.getHistory("bitcoin")).thenReturn(f)
        // When
        val flowResult = repository.getHistory("bitcoin")
        // Then
        val data = flowResult.first()
        assert(data.isNotEmpty())
        assert(data[0].quoteId == "bitcoin")
    }

    @Test
    fun `Get asset price history from Dao`() {
        // Given
        val f = flowOf<List<PriceEntity>>(listOf())
        `when`(dao.getHistory("bitcoin")).thenReturn(f)
        // When
        repository.getHistory("bitcoin")
        // Then
        verify(dao).getHistory("bitcoin")
    }

    @Test
    fun `Fetch asset price history from API`() = runTest {
        // Given
        val dto = HistoryPriceDTO(priceUsd = BigDecimal(1.0), time = 1, date = "")
        val apiResult = Response.success(
            AssetHistoryDTO(listOf(dto))
        )
        `when`(api.getHistory("bitcoin")).thenReturn(apiResult)

        // When
        repository.fetchHistory("bitcoin")

        // Then
        val pricesList = listOf(PriceEntity(1,"bitcoin",1.0))
        verify(api, times(1)).getHistory("bitcoin")
        verify(dao, times(1)).insertPrice(pricesList)
    }


    private fun getFakeAssetEntity(): AssetEntity {
        return AssetEntity(
            id = "bitcoin",
            rank = 1,
            symbol = "BTC",
            name = "Bitcoin",
            supply = 0.0,
            maxSupply = 0,
            marketCapUsd = 10000,
            volumeUsd24Hr = 10000,
            priceUsd = 0.0,
            changePercent24Hr = 0.0,
            vwap24Hr = 0.0,
            explorer = "data",
        )
    }
    private fun getAssetDTO(): AssetDTO {
        return AssetDTO(
            rank = 1,
            id = "bitcoin",
            symbol = "BTC",
            name = "Bitcoin",
            supply = BigDecimal(0),
            maxSupply = BigDecimal(0),
            marketCapUsd = BigDecimal(0),
            volumeUsd24Hr = BigDecimal(0),
            priceUsd = BigDecimal(0),
            changePercent24Hr = BigDecimal(0),
            vwap24Hr = BigDecimal(0),
            explorer = "data",
        )
    }
}