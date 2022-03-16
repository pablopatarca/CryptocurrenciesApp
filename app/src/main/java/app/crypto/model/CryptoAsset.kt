package app.crypto.model

data class CryptoAsset(
    val id: String,
    val rank: Int,
    val symbol: String,
    val name: String,
    val supply: Double,
    val maxSupply: Long,
    val marketCapUsd: Long,
    val volumeUsd24Hr: Long,
    val priceUsd: Double,
    val changePercent24Hr: Double,
    val vwap24Hr: Double,
    val explorer: String?
)