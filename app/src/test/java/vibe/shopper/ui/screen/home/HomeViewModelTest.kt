package vibe.shopper.ui.screen.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.whenever
import vibe.shopper.data.model.Chair
import vibe.shopper.data.model.ChairInfo
import vibe.shopper.data.model.Couch
import vibe.shopper.data.model.CouchInfo
import vibe.shopper.data.model.Failure
import vibe.shopper.data.model.Price
import vibe.shopper.data.model.Success
import vibe.shopper.domain.AddToCartUseCase
import vibe.shopper.domain.GetCartItemCountUseCase
import vibe.shopper.domain.GetProductsUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var addToCartUseCase: AddToCartUseCase
    private lateinit var getCartItemCountUseCase: GetCartItemCountUseCase

    private val cartItemCountFlow = MutableStateFlow(0)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getProductsUseCase = Mockito.mock(GetProductsUseCase::class.java)
        addToCartUseCase = Mockito.mock(AddToCartUseCase::class.java)
        getCartItemCountUseCase = Mockito.mock(GetCartItemCountUseCase::class.java)

        whenever(getCartItemCountUseCase.getCount()).thenReturn(cartItemCountFlow)

        homeViewModel = HomeViewModel(getProductsUseCase, addToCartUseCase, getCartItemCountUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getProducts() - success updates UI state with products`() = runTest {
        val products = fakeProducts()

        whenever(getProductsUseCase.getProducts(anyOrNull())).thenReturn(Success(products))

        homeViewModel.getProducts() // ✅ Trigger function

        homeViewModel.homeUiState.test {
            awaitItem()
            awaitItem()
            val homeUiState = awaitItem()

            assertTrue(homeUiState.isLoading.not())
            assertEquals(products, homeUiState.products)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getProducts() - failure updates UI state with error message`() = runTest {
        whenever(getProductsUseCase.getProducts(anyOrNull())).thenReturn(Failure(Exception()))

        homeViewModel.getProducts()

        homeViewModel.homeUiState.test {
            awaitItem()
            awaitItem()
            val homeUiState = awaitItem()

            assertTrue(homeUiState.isLoading.not())
            assertNotNull(homeUiState.messageResId)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onMessageShown() - clears error message`() = runTest {
        homeViewModel.onMessageShown()
        val state = homeViewModel.homeUiState.value
        assert(state.messageResId == null) { "Expected messageResId to be null but was ${state.messageResId}" }
    }

    @Test
    fun `onProductClicked() - updates selected product`() = runTest {
        val product = fakeProducts().first()
        homeViewModel.onProductClicked(product)

        homeViewModel.productUiState.test {
            awaitItem()
            val productUiState = awaitItem()

            assert(productUiState.product == product) { "Expected selected product to be $product but was ${productUiState.product}" }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addProductToCart() - calls addToCartUseCase`() = runTest {
        val productId = 1
        homeViewModel.addProductToCart(productId)
        verify(addToCartUseCase).addToCart(productId)
    }

    @Test
    fun `getProducts(query) - success updates UI state with filtered products!!!`() = runTest {
        val query = "Henriksdal"
        val products = fakeProducts()
        val filteredProducts = products.filter { it.name.contains(query, ignoreCase = true) }

        whenever(getProductsUseCase.getProducts(query)).thenReturn(Success(filteredProducts))

        homeViewModel.getProducts(query)

        homeViewModel.homeUiState.test {
            awaitItem()
            awaitItem()
            val homeUiState = awaitItem()

            assertEquals(filteredProducts, homeUiState.products)
            assertEquals(query, homeUiState.searchQuery)
            assertFalse(homeUiState.isLoading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getProducts(query) - success updates UI state with filtered products`() = runTest {
        val query = "Henriksdal"
        val products = fakeProducts()
        val filteredProducts = products.filter { it.name.contains(query, ignoreCase = true) }

        whenever(getProductsUseCase.getProducts(query)).thenReturn(Success(filteredProducts))

        homeViewModel.getProducts(query)

        homeViewModel.homeUiState.test {
            awaitItem()
            awaitItem()
            val homeUiState = awaitItem()

            assertEquals(filteredProducts, homeUiState.products)
            assertEquals(query, homeUiState.searchQuery)
            assertFalse(homeUiState.isLoading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `changeLoadingTo() - isLoading changes to true then to false while getting products`() = runTest {
        whenever(getProductsUseCase.getProducts(anyOrNull())).thenReturn(Failure(Exception()))

        homeViewModel.getProducts()

        homeViewModel.homeUiState.test {
            awaitItem()
            val loadingState = awaitItem()
            val notLoadingState = awaitItem()

            assertTrue(loadingState.isLoading)
            assertFalse(notLoadingState.isLoading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun fakeProducts() = listOf(
        Chair(
            id = 1,
            name = "Henriksdal",
            price = Price(100.0, "kr"),
            type = "chair",
            imageUrl = "https://shop.static.ingka.ikea.com/PIAimages/0462849_PE608354_S4.JPG",
            info = ChairInfo(
                material = "wood",
                color = "black",
            ),
        ),
        Couch(
            id = 1,
            name = "Lidhult",
            price = Price(1035.0, "kr"),
            type = "couch",
            imageUrl = "https://shop.static.ingka.ikea.com/PIAimages/0667779_PE714073_S4.JPG",
            info = CouchInfo(
                numberOfSeats = 4,
                color = "beige",
            ),
        ),
    )
}
