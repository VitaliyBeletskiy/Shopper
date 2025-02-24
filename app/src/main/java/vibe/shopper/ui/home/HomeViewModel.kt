package vibe.shopper.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import vibe.shopper.domain.GetProductsUseCase
import javax.inject.Inject

@Suppress("ktlint:standard:annotation")
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
) : ViewModel() {

    fun getProducts() {
        viewModelScope.launch {
            val result = getProductsUseCase.getProducts()
            Log.d("vitDebug", "getProducts: $result")
        }
    }
}
