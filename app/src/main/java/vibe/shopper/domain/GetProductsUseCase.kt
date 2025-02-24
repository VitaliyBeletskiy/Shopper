package vibe.shopper.domain

import vibe.shopper.data.ProductRepository
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
                Success(it.products)
            },
            ifFailure = {
                Failure(it)
            },
        )
}
