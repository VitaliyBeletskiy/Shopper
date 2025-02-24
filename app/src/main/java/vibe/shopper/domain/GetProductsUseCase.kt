package vibe.shopper.domain

import vibe.shopper.data.ProductRepository
import vibe.shopper.data.model.Chair
import vibe.shopper.data.model.Couch
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
    suspend fun getProducts(): Result<List<Product>, Exception> =
        productRepository.getApiProducts().fold(
            ifSuccess = {
                val products = it.products.map { product ->
                    val url =
                        "https://www.ikea.com/se/sv/images/products/oestanoe-stol-svart-remmarn-moerkgra__1119282_pe873451_s5.jpg?f=xs"
                    if (product is Chair) {
                        product.copy(imageUrl = url)
                    } else {
//                        (product as Couch).copy(imageUrl = url)
                        product
                    }
                }
                Success(products)
//                Success(it.products)
            },
            ifFailure = {
                Failure(it)
            },
        )
}
