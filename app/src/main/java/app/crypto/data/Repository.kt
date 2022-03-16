package app.crypto.data

import kotlinx.coroutines.flow.Flow
import java.io.IOException

interface Repository {

    // Asset
    fun getAssets(): Flow<List<AssetEntity>>

    @Throws(IOException::class)
    suspend fun fetchAssets()

    suspend fun getAssetById(id: String): AssetEntity?

    // History
    fun getHistory(id: String): Flow<List<PriceEntity>>
    @Throws(IOException::class)
    suspend fun fetchHistory(id: String)
}