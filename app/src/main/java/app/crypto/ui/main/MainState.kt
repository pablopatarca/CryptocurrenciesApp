package app.crypto.ui.main

import app.crypto.model.CryptoAsset


data class MainState(
    var cryptoList: List<CryptoAsset>
)