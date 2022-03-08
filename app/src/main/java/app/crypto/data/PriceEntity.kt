package app.crypto.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PriceEntity(
    @PrimaryKey val time: Long,
    val quoteId: String,
    val priceUsd: Double
)
