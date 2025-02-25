package vibe.shopper.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vibe.shopper.data.datasource.CartDataSource
import vibe.shopper.data.model.CartItem
import vibe.shopper.data.model.fold
import javax.inject.Inject

interface CartRepository {
    val cartItems: StateFlow<List<CartItem>>
    fun addToCart(productId: Int)
    fun removeFromCart(productId: Int)
    fun changeProductQuantity(productId: Int, quantity: Int)
}

@Suppress("ktlint:standard:annotation")
class CartRepositoryImpl @Inject constructor(
    private val cartDataSource: CartDataSource,
    private val productRepository: ProductRepository,
    private val repoScope: CoroutineScope,
) : CartRepository {

    private val idsToQuantity = mutableMapOf<Int, Int>()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    override val cartItems: StateFlow<List<CartItem>> get() = _cartItems

    init {
        getCart()
    }

    override fun addToCart(productId: Int) {
        // if this product is already in the cart, just increase the quantity
        idsToQuantity[productId]?.let {
            idsToQuantity[productId] = it + 1
            cartDataSource.saveCart(idsToQuantity)
            val updatedCart = _cartItems.value.map { cartItem ->
                if (cartItem.id == productId) {
                    cartItem.copy(quantity = cartItem.quantity + 1)
                } else {
                    cartItem
                }
            }
            _cartItems.update { updatedCart }
            return
        }
        // it it's new, add it to the cart with quantity 1
        repoScope.launch {
            idsToQuantity[productId] = 1
            cartDataSource.saveCart(idsToQuantity)

            val newCartItem = productRepository.getProduct(productId).fold(
                ifSuccess = { product ->
                    CartItem(
                        id = productId,
                        quantity = 1,
                        product = product,
                    )
                },
                ifFailure = { _ ->
                    CartItem(
                        id = productId,
                        quantity = 1,
                        product = null,
                    )
                },
            )
            _cartItems.value += newCartItem
        }
    }

    override fun removeFromCart(productId: Int) {
        idsToQuantity.remove(productId)
        cartDataSource.saveCart(idsToQuantity)
        val updatedCart = _cartItems.value.filter { it.id != productId }
        _cartItems.update { updatedCart }
    }

    override fun changeProductQuantity(productId: Int, quantity: Int) {
        idsToQuantity[productId] = quantity
        cartDataSource.saveCart(idsToQuantity)
        val updatedCart = _cartItems.value.map { cartItem ->
            if (cartItem.id == productId) {
                cartItem.copy(quantity = quantity)
            } else {
                cartItem
            }
        }
        _cartItems.update { updatedCart }
    }

    private fun getCart() {
        idsToQuantity.clear()
        idsToQuantity.putAll(cartDataSource.getCart())
        repoScope.launch {
            val cartItems = idsToQuantity.entries.map { (productId, quantity) ->
                productRepository.getProduct(productId).fold(
                    ifSuccess = { product ->
                        CartItem(
                            id = productId,
                            quantity = quantity,
                            product = product,
                        )
                    },
                    ifFailure = { _ ->
                        CartItem(
                            id = productId,
                            quantity = quantity,
                            product = null,
                        )
                    },
                )
            }
            _cartItems.value = cartItems
        }
    }
}
