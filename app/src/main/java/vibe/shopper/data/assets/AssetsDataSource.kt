package vibe.shopper.data.assets

import android.app.Application
import javax.inject.Inject

interface AssetsDataSource {
    suspend fun getProducts(): String
}

@Suppress("ktlint:standard:annotation")
class AssetsDataSourceImpl @Inject constructor(
    private val application: Application,
) : AssetsDataSource {

    companion object {
        private const val FILE_NAME = "products.json"
    }

    override suspend fun getProducts(): String {
        application.assets.open(FILE_NAME).use { inputStream ->
            return inputStream.bufferedReader().use { it.readText() }
        }
    }
}
