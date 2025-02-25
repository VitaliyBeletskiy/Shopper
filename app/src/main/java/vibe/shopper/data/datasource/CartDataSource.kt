package vibe.shopper.data.datasource

import android.app.Application
import javax.inject.Inject

interface CartDataSource {
    fun saveCart(cart: Map<Int, Int>)
    fun getCart(): Map<Int, Int>
}

@Suppress("ktlint:standard:annotation")
class SharedPrefsCartDataSourceImpl @Inject constructor(
    private val application: Application,
) : CartDataSource {

    companion object {
        private const val APP_SHARED_PREFS = "shopper_cart"
    }

    override fun saveCart(cart: Map<Int, Int>) {
        clearSharedPrefs()
        application.getSharedPreferences(APP_SHARED_PREFS, Application.MODE_PRIVATE).edit().apply {
            cart.forEach { (productId, quantity) ->
                putInt(productId.toString(), quantity)
            }
            apply()
        }
    }

    override fun getCart(): Map<Int, Int> {
        val sharedPreferences =
            application.getSharedPreferences(APP_SHARED_PREFS, Application.MODE_PRIVATE)
        return sharedPreferences.all.map { (productId, quantity) ->
            productId.toInt() to quantity as Int
        }.toMap()
    }

    private fun clearSharedPrefs() {
        application.getSharedPreferences(APP_SHARED_PREFS, Application.MODE_PRIVATE).edit().apply {
            clear()
            apply()
        }
    }
}
