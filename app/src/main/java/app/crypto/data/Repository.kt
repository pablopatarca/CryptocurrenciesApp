package app.crypto.data

import kotlinx.coroutines.flow.Flow

interface Repository {

    // Asset
    fun getAssets(): Flow<List<AssetEntity>>
    suspend fun fetchAssets()
    suspend fun getAssetById(id: String): AssetEntity?

    // History
    fun getHistory(id: String): Flow<List<PriceEntity>>
    suspend fun fetchHistory(id: String)
}