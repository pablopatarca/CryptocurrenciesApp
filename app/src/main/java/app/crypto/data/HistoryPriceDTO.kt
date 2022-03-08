package app.crypto.data

import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class HistoryPriceDTO(
    val priceUsd: BigDecimal,
    val time: Long,
    val date: String
)
