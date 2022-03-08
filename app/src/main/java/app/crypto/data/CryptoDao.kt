package app.crypto.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoDao {

    @Query("SELECT * FROM AssetEntity")
    fun getAssets(): Flow<List<AssetEntity>>

    @Query("SELECT * FROM AssetEntity where id = :id")
    suspend fun getAssetById(id: String): AssetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asset: AssetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asset: List<AssetEntity>)

    @Delete
    suspend fun delete(asset: AssetEntity)


    @Query("SELECT * FROM PriceEntity WHERE quoteId = :quoteId")
    fun getHistory(quoteId: String): Flow<List<PriceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrice(price: List<PriceEntity>)
}