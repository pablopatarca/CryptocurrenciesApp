package app.crypto.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.crypto.data.AssetEntity
import app.crypto.data.PriceEntity
import app.crypto.data.Repository
import app.crypto.model.CryptoAsset
import app.crypto.utils.CoroutineDispatchers
import app.crypto.utils.DefaultDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val repository: Repository,
    savedStateHandle: SavedStateHandle,
    private val coroutineDispatchers: CoroutineDispatchers = DefaultDispatchers
) : ViewModel() {

    private val _messages = MutableSharedFlow<String>()
    val messages = _messages.asSharedFlow()

    private val cryptoId = MutableStateFlow(savedStateHandle.get<String>("id") ?: "")

    val state: StateFlow<CryptoAsset?> = cryptoId.mapLatest {
        val crypto = repository.getAssetById(it) ?: return@mapLatest null
        crypto.toAsset()
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val history: StateFlow<List<PriceEntity>> = state.flatMapLatest {
        val id = it?.id ?: return@flatMapLatest flow { listOf<List<PriceEntity>>() }
        fetchInfo(id)
        repository.getHistory(id)
    }.stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    fun fetchInfo(id: String){
        viewModelScope.launch(coroutineDispatchers.IO) {
            try {
                repository.fetchHistory(id = id)
            } catch (e: Exception){
                _messages.emit(e.message ?: "")
            }
        }
    }

    private fun AssetEntity.toAsset(): CryptoAsset {
        return with(this){
            CryptoAsset(
                id = id,
                rank = rank,
                symbol = symbol,
                name = name,
                supply = supply,
                maxSupply = maxSupply,
                marketCapUsd = marketCapUsd,
                volumeUsd24Hr = volumeUsd24Hr,
                priceUsd = priceUsd,
                changePercent24Hr = changePercent24Hr,
                vwap24Hr = vwap24Hr,
                explorer = explorer  ?: "",
            )
        }
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}