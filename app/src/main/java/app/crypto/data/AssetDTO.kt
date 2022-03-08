package app.crypto.data

import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class AssetDTO(
    val rank: Int,
    val id: String,
    val symbol: String,
    val name: String,
    val supply: BigDecimal?,
    val maxSupply: BigDecimal?,
    val marketCapUsd: BigDecimal?,
    val volumeUsd24Hr: BigDecimal?,
    val priceUsd: BigDecimal?,
    val changePercent24Hr: BigDecimal?,
    val vwap24Hr: BigDecimal?,
    val explorer: String?
)
