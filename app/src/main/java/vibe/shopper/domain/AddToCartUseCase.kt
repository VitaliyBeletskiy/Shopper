package vibe.shopper.domain

import vibe.shopper.data.CartRepository
import javax.inject.Inject

@Suppress("ktlint:standard:annotation")
class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository,
) {
    fun addToCart(productId: Int) {
        cartRepository.addToCart(productId)
    }
}
