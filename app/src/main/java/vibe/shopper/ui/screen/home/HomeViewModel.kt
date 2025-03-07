package vibe.shopper.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vibe.shopper.R
import vibe.shopper.data.model.Product
import vibe.shopper.data.model.fold
import vibe.shopper.domain.AddToCartUseCase
import vibe.shopper.domain.GetCartItemCountUseCase
import vibe.shopper.domain.GetProductsUseCase
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val messageResId: Int? = null,
    val searchQuery: String? = null,
    val cartItemCount: Int = 0,
)

data class ProductUiState(
    val product: Product? = null,
    val cartItemCount: Int = 0,
)

@Suppress("ktlint:standard:annotation")
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getCartItemCountUseCase: GetCartItemCountUseCase,
) : ViewModel() {

    private var getProductsJob: Job? = null

    private val cartItemCountFlow = getCartItemCountUseCase.getCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)
    private val _homeUiState = MutableStateFlow(HomeUiState())
    private val _productUiState = MutableStateFlow(ProductUiState())

    val homeUiState: StateFlow<HomeUiState> = combine(
        _homeUiState.onStart { emit(HomeUiState()) },
        cartItemCountFlow.onStart { emit(0) },
    ) { uiState, cartItemCount ->
        uiState.copy(cartItemCount = cartItemCount)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        HomeUiState(),
    )

    val productUiState: StateFlow<ProductUiState> = combine(
        _productUiState.onStart { emit(ProductUiState()) },
        cartItemCountFlow.onStart { emit(0) },
    ) { uiState, cartItemCount ->
        uiState.copy(cartItemCount = cartItemCount)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        ProductUiState(),
    )

    fun getProducts(query: String? = null) {
        if (getProductsJob?.isActive == true) getProductsJob?.cancel()

        getProductsJob = viewModelScope.launch {
            changeLoadingTo(true)
            delay(500) // FIXME: so we can see the progress indicator
            getProductsUseCase.getProducts(query).fold(
                ifSuccess = { products ->
                    _homeUiState.update {
                        _homeUiState.value.copy(
                            products = products,
                            searchQuery = query,
                        )
                    }
                },
                ifFailure = { _ ->
                    _homeUiState.update { _homeUiState.value.copy(messageResId = R.string.cannot_get_products) }
                },
            )
            changeLoadingTo(false)
        }
    }

    fun onMessageShown() {
        _homeUiState.update { it.copy(messageResId = null) }
    }

    private fun changeLoadingTo(loading: Boolean) {
        _homeUiState.update { _homeUiState.value.copy(isLoading = loading) }
    }

    fun onProductClicked(product: Product) {
        _productUiState.update { _productUiState.value.copy(product = product) }
    }

    fun addProductToCart(productId: Int) {
        addToCartUseCase.addToCart(productId)
    }

    fun refreshProducts() {
        getProducts(query = _homeUiState.value.searchQuery)
    }
}
