package app.crypto.ui

import app.crypto.BuildConfig


fun getImageUrl(assetId: String): String {
    return BuildConfig.IMAGE_URL.format(assetId.lowercase())
}
