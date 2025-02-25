package vibe.shopper.ui.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import vibe.shopper.data.CartRepository
import vibe.shopper.data.model.CartItem
import javax.inject.Inject

data class CartUiState(
    val cartItems: List<CartItem>,
    val totalPrice: Double,
)

@Suppress("ktlint:standard:annotation")
@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
) : ViewModel() {

    val cartUiState: StateFlow<CartUiState> = cartRepository.cartItems.map { cartItems ->
        val totalPrice = cartItems.sumOf { (it.product?.price?.value ?: 0.0) * it.quantity }
        CartUiState(
            cartItems = cartItems,
            totalPrice = totalPrice,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CartUiState(emptyList(), 0.0),
    )
}
