package vibe.shopper.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Product {
    abstract val id: Int
    abstract val name: String
    abstract val price: Price
    abstract val type: String
    abstract val imageUrl: String
}

@Serializable
data class Price(
    val value: Double,
    val currency: String,
)

@Serializable
@SerialName("chair")
data class Chair(
    override val id: Int,
    override val name: String,
    override val price: Price,
    override val type: String,
    override val imageUrl: String,
    val info: ChairInfo,
) : Product()

@Serializable
data class ChairInfo(
    val material: String,
    val color: String,
)

@Serializable
@SerialName("couch")
data class Couch(
    override val id: Int,
    override val name: String,
    override val price: Price,
    override val type: String,
    override val imageUrl: String,
    val info: CouchInfo,
) : Product()

@Serializable
data class CouchInfo(
    val numberOfSeats: Int,
    val color: String,
)
