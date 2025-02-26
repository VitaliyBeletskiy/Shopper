package vibe.shopper.domain

import kotlinx.coroutines.flow.Flow
import vibe.shopper.data.CartRepository
import javax.inject.Inject

@Suppress("ktlint:standard:annotation")
class GetCartItemCountUseCase @Inject constructor(
    private val cartRepository: CartRepository,
) {
    fun getCount(): Flow<Int> = cartRepository.cartItemCount
}
