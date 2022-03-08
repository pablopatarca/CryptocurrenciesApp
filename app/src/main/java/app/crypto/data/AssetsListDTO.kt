package app.crypto.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AssetsListDTO(
    val data: List<AssetDTO>
)
