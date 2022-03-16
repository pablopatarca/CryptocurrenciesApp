package app.crypto.data

import kotlinx.coroutines.flow.Flow
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: CryptoAPI,
    private val dao: CryptoDao
): Repository {

    override fun getAssets(): Flow<List<AssetEntity>> {
        return dao.getAssets()
    }

    @Throws(IOException::class)
    override suspend fun fetchAssets(){
        val result = api.getAssets().getNetworkResult()
        val list = result.toAssetsEntities()
        dao.insert(list)
    }

    override suspend fun getAssetById(id: String): AssetEntity? {
        return dao.getAssetById(id)
    }

    override fun getHistory(id: String): Flow<List<PriceEntity>> {
        return dao.getHistory(id)
    }

    @Throws(IOException::class)
    override suspend fun fetchHistory(id: String) {
        val result = api.getHistory(quoteId = id).getNetworkResult()
        val list = result.toPriceEntities(id)
        dao.insertPrice(list)
    }

    private fun AssetsListDTO.toAssetsEntities(): List<AssetEntity> {
        return data.map {
            with(it){
                AssetEntity(
                    rank = rank,
                    id = id,
                    symbol = symbol,
                    name = name,
                    supply = supply.scaleToDouble(),
                    maxSupply = maxSupply?.toLong() ?: 0,
                    marketCapUsd = marketCapUsd?.toLong() ?: 0,
                    volumeUsd24Hr = volumeUsd24Hr?.toLong() ?: 0,
                    priceUsd = priceUsd.scaleToDouble(4),
                    changePercent24Hr = changePercent24Hr.scaleToDouble(),
                    vwap24Hr = vwap24Hr.scaleToDouble(),
                    explorer = explorer  ?: "",
                )
            }
        }
    }

    private fun AssetHistoryDTO.toPriceEntities(
        id: String
    ): List<PriceEntity> {
        return data.map {
            with(it){
                PriceEntity(
                    quoteId = id,
                    priceUsd = priceUsd.toDouble(),
                    time = time
                )
            }
        }
    }

    private fun BigDecimal?.scaleToDouble(scale: Int = 2): Double {
        return this?.setScale(scale, RoundingMode.CEILING)
            ?.toDouble() ?: 0.0
    }
}