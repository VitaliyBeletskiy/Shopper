package vibe.shopper.data

import vibe.shopper.data.model.Chair
import vibe.shopper.data.model.ChairInfo
import vibe.shopper.data.model.Failure
import vibe.shopper.data.model.Price
import vibe.shopper.data.model.Product
import vibe.shopper.data.model.Result
import vibe.shopper.data.model.Success

class FakeProductRepository : ProductRepository {

    private val fakeProducts = listOf(
        Chair(
            id = 1,
            name = "Henriksdal",
            price = Price(100.0, "kr"),
            type = "chair",
            imageUrl = "https://www.ikea.com/se/sv/images/products/oestanoe-stol-svart-remmarn-moerkgra__1119282_pe873451_s5.jpg?f=xs",
            info = ChairInfo(
                color = "black",
                material = "plastic",
            ),
        ),
    )

    override suspend fun getApiProducts(query: String?): Result<List<Product>, Exception> =
        if (query.isNullOrEmpty()) {
            Success(fakeProducts)
        } else {
            val filtered = fakeProducts.filter { it.name.contains(query, ignoreCase = true) }
            if (filtered.isNotEmpty()) Success(filtered) else Failure(Exception("No products found"))
        }

    override suspend fun getProduct(productId: Int): Result<Product, Unit> {
        val product = fakeProducts.find { it.id == productId }
        return if (product != null) Success(product) else Failure(Unit)
    }
}
