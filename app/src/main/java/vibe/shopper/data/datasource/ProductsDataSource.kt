package vibe.shopper.data.datasource

import android.app.Application
import android.util.Log
import kotlinx.serialization.json.Json
import vibe.shopper.data.model.ApiProducts
import vibe.shopper.data.model.Failure
import vibe.shopper.data.model.Product
import vibe.shopper.data.model.Result
import vibe.shopper.data.model.Success
import vibe.shopper.data.model.fold
import javax.inject.Inject

interface ProductsDataSource {
    suspend fun getProducts(): Result<ApiProducts, Exception>
    suspend fun getProduct(productId: Int): Result<Product, Unit>
}

@Suppress("ktlint:standard:annotation")
class AssetsProductsDataSourceImpl @Inject constructor(
    private val application: Application,
) : ProductsDataSource {

    companion object {
        private const val FILE_NAME = "products.json"
    }

    override suspend fun getProducts(): Result<ApiProducts, Exception> =
        try {
            val jsonString = application.assets.open(FILE_NAME).use { inputStream ->
                inputStream.bufferedReader().use { it.readText() }
            }
            val json = Json { ignoreUnknownKeys = true }
            val apiProducts = json.decodeFromString<ApiProducts>(jsonString)
            Success(apiProducts)
        } catch (e: Exception) {
            Failure(e)
        }

    override suspend fun getProduct(productId: Int): Result<Product, Unit> {
        getProducts().fold(
            ifSuccess = { apiProducts ->
                apiProducts.products.find { it.id == productId }?.let { return Success(it) }
                return Failure(Unit)
            },
            ifFailure = {
                return Failure(Unit)
            },
        )
    }
}
