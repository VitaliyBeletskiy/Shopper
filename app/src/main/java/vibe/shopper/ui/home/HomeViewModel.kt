package vibe.shopper.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import vibe.shopper.data.ProductRepository
import javax.inject.Inject

@Suppress("ktlint:standard:annotation")
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {

    fun getProducts() {
        viewModelScope.launch {
            val result = productRepository.getProducts()
            Log.d("vitDebug", "getProducts: $result")
        }
    }
}
