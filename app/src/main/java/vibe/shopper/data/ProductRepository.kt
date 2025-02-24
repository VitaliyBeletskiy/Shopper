package vibe.shopper.data

import vibe.shopper.data.assets.AssetsDataSource
import javax.inject.Inject

interface ProductRepository {
    suspend fun getProducts(): String
}

@Suppress("ktlint:standard:annotation")
class ProductRepositoryImpl @Inject constructor(
    private val assetsDataSource: AssetsDataSource,
) : ProductRepository {

    override suspend fun getProducts(): String = assetsDataSource.getProducts()
}
