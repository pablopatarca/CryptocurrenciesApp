package app.crypto.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class PriceEntityReadWriteTest {
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
    fun writePriceAndReadInList() = runTest {
        val price: List<PriceEntity> = listOf(
            PriceEntity(time = 1, "bitcoin", 0.0)
        )

        cryptoDao.insertPrice(price)
        val byQuoteId = cryptoDao.getHistory("bitcoin").first()

        assert(byQuoteId[0] == price.first())
    }
}