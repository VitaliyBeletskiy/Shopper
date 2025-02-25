package vibe.shopper.data.model

data class CartItem(
    val id: Int,
    val quantity: Int,
    val product: Product?,
)
