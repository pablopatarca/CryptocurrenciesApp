package app.crypto.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AssetHistoryDTO(
    val data: List<HistoryPriceDTO>
)
