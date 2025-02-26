package vibe.shopper.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vibe.shopper.data.model.CartItem

class FakeCartRepository : CartRepository {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    override val cartItems: StateFlow<List<CartItem>> get() = _cartItems

    private val cartMap = mutableMapOf<Int, CartItem>()

    override fun addToCart(productId: Int) {
        val existingItem = cartMap[productId]
        if (existingItem == null) {
            val newCartItem = CartItem(id = productId, quantity = 1, product = null)
            cartMap[productId] = newCartItem
        } else {
            cartMap[productId] = existingItem.copy(quantity = existingItem.quantity + 1)
        }
        updateCartFlow()
    }

    override fun removeFromCart(productId: Int) {
        cartMap.remove(productId)
        updateCartFlow()
    }

    override fun changeProductQuantity(productId: Int, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
        } else {
            cartMap[productId]?.let {
                cartMap[productId] = it.copy(quantity = quantity)
            }
        }
        updateCartFlow()
    }

    private fun updateCartFlow() {
        _cartItems.value = cartMap.values.toList()
    }
}
