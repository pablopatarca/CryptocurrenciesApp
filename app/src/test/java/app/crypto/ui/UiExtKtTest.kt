package app.crypto.ui

import org.junit.Test

class UiExtKtTest {

    @Test
    fun getImageUrl() {
        val assetId = "assetId1234"
        val imageUrl = getImageUrl(assetId = assetId)
        println(imageUrl)
        assert(imageUrl.contains(assetId, true))
    }
}