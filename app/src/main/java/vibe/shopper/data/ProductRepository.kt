package vibe.shopper.data

import vibe.shopper.data.assets.AssetsDataSource
import vibe.shopper.data.model.ApiProducts
import vibe.shopper.data.model.Result
import javax.inject.Inject

interface ProductRepository {
    suspend fun getApiProducts(): Result<ApiProducts, Exception>
}

@Suppress("ktlint:standard:annotation")
class ProductRepositoryImpl @Inject constructor(
    private val assetsDataSource: AssetsDataSource,
) : ProductRepository {

    override suspend fun getApiProducts(): Result<ApiProducts, Exception> =
        assetsDataSource.getProducts()
}
