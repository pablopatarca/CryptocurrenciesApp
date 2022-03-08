package app.crypto.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AssetEntity(
    @PrimaryKey val id: String,
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