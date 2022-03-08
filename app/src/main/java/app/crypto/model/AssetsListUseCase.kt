package app.crypto.model

import kotlinx.coroutines.flow.Flow

interface AssetsListUseCase {
    fun getSortedAssets(): Flow<List<CryptoAsset>>
}