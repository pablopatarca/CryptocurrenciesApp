package app.crypto.ui.main

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.crypto.model.CryptoAsset
import app.crypto.ui.MainActivity
import app.crypto.ui.theme.CryptoTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CryptoListTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun show_empty_list() {
        val state = mutableStateOf(MainState(listOf()))
        composeTestRule.setContent {
            CryptoTheme {
                CryptoList(state)
            }
        }

        composeTestRule.onNodeWithTag("LazyColumn")
            .onChildren()
            .assertCountEquals(0)
    }


    @Test
    fun Show_Crypto_List_With_Items() {
        val state = mutableStateOf(MainState(listOf(
            getCryptoAsset()
        )))
        composeTestRule.setContent {
            CryptoTheme {
                CryptoList(state)
            }
        }

        composeTestRule.onNodeWithTag("LazyColumn")
            .onChildren()
            .assertCountEquals(1)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("app.crypto", appContext.packageName)
    }

    private fun getCryptoAsset(): CryptoAsset {
        return CryptoAsset(
            "bitcoin",
            1,
            "btc",
            "Bitcoin",
            0.0,
            0,
            0,
            0,
            0.0,
            0.0,
            0.0,
            "")
    }
}