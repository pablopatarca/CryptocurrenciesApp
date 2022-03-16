package app.crypto.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.crypto.data.NetworkException
import app.crypto.data.Repository
import app.crypto.model.AssetsListUseCase
import app.crypto.ui.main.MainState
import app.crypto.utils.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    useCase: AssetsListUseCase,
    private val coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    // MutableSharedFlow to emit an state once
    private val _messages = MutableSharedFlow<String>()
    val messages = _messages.asSharedFlow()

    val state: StateFlow<MainState> = useCase.getSortedAssets()
        .mapLatest {
            MainState(it)
        }.catch { e ->
            //TODO: Handle exceptions
            _messages.emit(e.message ?: "Unknown Error 🙈")
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            MainState(listOf())
        )

    init {
        loadCryptoList()
    }

    fun loadCryptoList(){
        viewModelScope.launch(coroutineDispatchers.IO) {
            try {
                repository.fetchAssets()
            } catch (e: NetworkException){
                _messages.emit(e.message ?: "Network Exception 🙈")
            } catch (t: Throwable){
                _messages.emit("Unknown Error 🙈")
            }
        }
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}