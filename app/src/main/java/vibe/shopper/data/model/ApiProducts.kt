package vibe.shopper.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiProducts(
    val products: List<Product>,
)
