package app.crypto.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AssetEntity::class, PriceEntity::class],
    version = 1
)
abstract class CryptoDatabase: RoomDatabase() {

    abstract val cryptoDao: CryptoDao

    companion object {
        const val DATABASE_NAME = "CRYPTO_DATABASE"
    }
}