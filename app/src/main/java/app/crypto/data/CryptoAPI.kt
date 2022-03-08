package app.crypto.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoAPI {

    @GET("/v2/assets")
    suspend fun getAssets(): Response<AssetsListDTO>

    @GET("/v2/assets/{quoteId}/history")
    suspend fun getHistory(
        @Path("quoteId") quoteId: String,
        @Query("interval") interval: String = "d1"
    ): Response<AssetHistoryDTO>

}