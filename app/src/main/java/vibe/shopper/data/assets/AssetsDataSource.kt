package vibe.shopper.data.assets

import android.app.Application
import kotlinx.serialization.json.Json
import vibe.shopper.data.model.ApiProducts
import vibe.shopper.data.model.Failure
import vibe.shopper.data.model.Result
import vibe.shopper.data.model.Success
import javax.inject.Inject

interface AssetsDataSource {
    suspend fun getProducts(): Result<ApiProducts, Exception>
}

@Suppress("ktlint:standard:annotation")
class AssetsDataSourceImpl @Inject constructor(
    private val application: Application,
) : AssetsDataSource {

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
}
