package app.crypto.model

import app.crypto.data.AssetEntity
import app.crypto.data.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AssetsListUseCaseImpl @Inject constructor(
    private val repository: Repository
): AssetsListUseCase {

    override fun getSortedAssets(): Flow<List<CryptoAsset>> = repository.getAssets()
        .mapLatest { assets ->
            assets.sortedBy { it.rank }
        }.mapLatest {
            it.toUIEntity()
        }

    private fun List<AssetEntity>.toUIEntity(): List<CryptoAsset> {
        return map {
            with(it){
                CryptoAsset(
                    id = id,
                    rank = rank,
                    symbol = symbol,
                    name = name,
                    supply = supply,
                    maxSupply = maxSupply,
                    marketCapUsd = marketCapUsd,
                    volumeUsd24Hr = volumeUsd24Hr,
                    priceUsd = priceUsd,
                    changePercent24Hr = changePercent24Hr,
                    vwap24Hr = vwap24Hr,
                    explorer = explorer  ?: "",
                )
            }
        }
    }
}