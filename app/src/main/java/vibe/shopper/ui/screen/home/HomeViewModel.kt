package vibe.shopper.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vibe.shopper.R
import vibe.shopper.data.model.Product
import vibe.shopper.data.model.fold
import vibe.shopper.domain.GetProductsUseCase
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val messageResId: Int? = null,
)

@Suppress("ktlint:standard:annotation")
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState>
        get() = _uiState

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            changeLoadingTo(true)

            delay(1_000)
            getProductsUseCase.getProducts().fold(
                ifSuccess = { products ->
                    _uiState.update { _uiState.value.copy(products = products) }
                },
                ifFailure = { _ ->
                    _uiState.update { _uiState.value.copy(messageResId = R.string.cannot_get_products) }
                },
            )
            changeLoadingTo(false)
        }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(messageResId = null) }
    }

    private fun changeLoadingTo(loading: Boolean) {
        _uiState.update { _uiState.value.copy(isLoading = loading) }
    }
}
