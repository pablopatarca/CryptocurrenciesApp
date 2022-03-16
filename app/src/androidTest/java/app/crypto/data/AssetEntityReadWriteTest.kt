package app.crypto.data

import android.content.Context
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AssetEntityReadWriteTest {
    private lateinit var cryptoDao: CryptoDao
    private lateinit var db: CryptoDatabase

    @Before
    fun createDb() {
        Dispatchers.setMain(StandardTestDispatcher())
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, CryptoDatabase::class.java
        ).build()
        cryptoDao = db.cryptoDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        Dispatchers.resetMain()
        db.close()
    }

    @Test
    fun writeAssetsAndReadAllInList() = runTest {
        val assets: List<AssetEntity> = listOf(
            getFakeAssetEntity("bitcoin", 1),
            getFakeAssetEntity("ether", 2),
            getFakeAssetEntity("doge", 3)
        )

        cryptoDao.insert(assets)
        val assetsFlow = cryptoDao.getAssets()

        val result = assetsFlow.first()

        assert(result.size == 3)
    }

    @Test
    fun writeAssetAndReadInList() = runTest {
        val assets: List<AssetEntity> = listOf(
            getFakeAssetEntity("bitcoin", 1),
            getFakeAssetEntity("ether", 2)
        )

        cryptoDao.insert(assets)
        val bitcoin = cryptoDao.getAssetById("bitcoin")
        val ether = cryptoDao.getAssetById("ether")

        assert(bitcoin == assets.first())
        assert(ether == assets[1])
    }

    @Test
    fun writeAssetAndDeleteInList() = runTest {
        val assets: List<AssetEntity> = listOf(
            getFakeAssetEntity("bitcoin", 1),
            getFakeAssetEntity("ether", 2)
        )

        cryptoDao.insert(assets)
        // Delete the first one
        cryptoDao.delete(assets[0])
        val bitcoin = cryptoDao.getAssetById("bitcoin")

        assert(bitcoin == null)
    }

    private fun getFakeAssetEntity(id: String, rank: Int): AssetEntity {
        return AssetEntity(
            id = id,
            rank = rank,
            symbol = "",
            name = id.capitalize(Locale.current),
            supply = 0.0,
            maxSupply = 0,
            marketCapUsd = 10000,
            volumeUsd24Hr = 10000,
            priceUsd = 0.0,
            changePercent24Hr = 0.0,
            vwap24Hr = 0.0,
            explorer = "data",
        )
    }
}