package vibe.shopper.data

import vibe.shopper.data.datasource.ProductsDataSource
import vibe.shopper.data.model.Product
import vibe.shopper.data.model.Result
import javax.inject.Inject

interface ProductRepository {
    suspend fun getApiProducts(query: String?): Result<List<Product>, Exception>
    suspend fun getProduct(productId: Int): Result<Product, Unit>
}

@Suppress("ktlint:standard:annotation")
class ProductRepositoryImpl @Inject constructor(
    private val productsDataSource: ProductsDataSource,
) : ProductRepository {

    override suspend fun getApiProducts(query: String?): Result<List<Product>, Exception> =
        productsDataSource.getProducts(query)

    override suspend fun getProduct(productId: Int): Result<Product, Unit> =
        productsDataSource.getProduct(productId)
}
