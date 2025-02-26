package vibe.shopper.domain

import vibe.shopper.data.ProductRepository
import vibe.shopper.data.model.Chair
import vibe.shopper.data.model.Failure
import vibe.shopper.data.model.Product
import vibe.shopper.data.model.Result
import vibe.shopper.data.model.Success
import vibe.shopper.data.model.fold
import javax.inject.Inject

@Suppress("ktlint:standard:annotation")
class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository,
) {
    // FIXME: quick-and-dirty patch to display at least some images. For testing purposes only.
    suspend fun getProducts(query: String? = null): Result<List<Product>, Exception> =
        productRepository.getApiProducts(query).fold(
            ifSuccess = { products ->
                // Success(Function…)
                val productsWithImages = products.map { product ->
                    val url =
                        "https://www.ikea.com/se/sv/images/products/oestanoe-stol-svart-remmarn-moerkgra__1119282_pe873451_s5.jpg?f=xs"
                    if (product is Chair) {
                        product.copy(imageUrl = url)
                    } else {
                        product
                    }
                }
                Success(productsWithImages)
            },
            ifFailure = {
                Failure(it)
            },
        )
}
