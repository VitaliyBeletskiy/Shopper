package vibe.shopper.ui.screen.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import vibe.shopper.data.model.Chair
import vibe.shopper.data.model.ChairInfo
import vibe.shopper.data.model.Couch
import vibe.shopper.data.model.CouchInfo
import vibe.shopper.data.model.Failure
import vibe.shopper.data.model.Price
import vibe.shopper.data.model.Success
import vibe.shopper.domain.AddToCartUseCase
import vibe.shopper.domain.GetProductsUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var homeViewModel: HomeViewModel
    private val getProductsUseCase: GetProductsUseCase = mock()
    private val addToCartUseCase: AddToCartUseCase = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        homeViewModel = HomeViewModel(getProductsUseCase, addToCartUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getProducts() - success updates UI state with products`() = runTest {
        val products = fakeProducts()
        `when`(getProductsUseCase.getProducts()).thenReturn(Success(products))

        homeViewModel.getProducts()
        advanceUntilIdle() // Ensures coroutine execution completes

        val state = homeViewModel.homeUiState.value
        assert(state.products == products) { "Expected products to be $products but was ${state.products}" }
        assert(!state.isLoading) { "Expected isLoading to be false but was ${state.isLoading}" }
    }

    @Test
    fun `getProducts() - failure updates UI state with error message`() = runTest {
        `when`(getProductsUseCase.getProducts()).thenReturn(Failure(Exception()))

        homeViewModel.getProducts()
        advanceUntilIdle()

        val state = homeViewModel.homeUiState.value
        assert(state.messageResId != null) { "Expected error message but got ${state.messageResId}" }
        assert(!state.isLoading) { "Expected isLoading to be false but was ${state.isLoading}" }
    }

    @Test
    fun `onMessageShown() - clears error message`() = runTest {
        // have to keep it here as getProducts() is called in init block
        `when`(getProductsUseCase.getProducts()).thenReturn(Success(fakeProducts()))

        homeViewModel.onMessageShown()
        val state = homeViewModel.homeUiState.value
        assert(state.messageResId == null) { "Expected messageResId to be null but was ${state.messageResId}" }
    }

    @Test
    fun `onProductClicked() - updates selected product`() = runTest {
        // have to keep it here as getProducts() is called in init block
        `when`(getProductsUseCase.getProducts()).thenReturn(Success(fakeProducts()))

        val product = fakeProducts().first()
        homeViewModel.onProductClicked(product)
        val state = homeViewModel.productUiState.value
        assert(state.product == product) { "Expected selected product to be $product but was ${state.product}" }
    }

    @Test
    fun `addProductToCart() - calls use case`() = runTest {
        // have to keep it here as getProducts() is called in init block
        `when`(getProductsUseCase.getProducts()).thenReturn(Success(fakeProducts()))

        val productId = 1
        homeViewModel.addProductToCart(productId)
        verify(addToCartUseCase).addToCart(productId)
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

// import androidx.arch.core.executor.testing.InstantTaskExecutorRule
// import kotlinx.coroutines.Dispatchers
// import kotlinx.coroutines.ExperimentalCoroutinesApi
// import kotlinx.coroutines.test.StandardTestDispatcher
// import kotlinx.coroutines.test.advanceUntilIdle
// import kotlinx.coroutines.test.resetMain
// import kotlinx.coroutines.test.runTest
// import kotlinx.coroutines.test.setMain
// import org.junit.After
// import org.junit.Before
// import org.junit.Rule
// import org.junit.Test
// import org.mockito.Mockito.mock
// import org.mockito.Mockito.`when`
// import vibe.shopper.data.model.Chair
// import vibe.shopper.data.model.ChairInfo
// import vibe.shopper.data.model.Couch
// import vibe.shopper.data.model.CouchInfo
// import vibe.shopper.data.model.Price
// import vibe.shopper.data.model.Success
// import vibe.shopper.domain.GetProductsUseCase

// @ExperimentalCoroutinesApi
// class HomeViewModelTest {
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private val testDispatcher = StandardTestDispatcher()
//
//    @Mock
//    private lateinit var getProductsUseCase: GetProductsUseCase
//
//    @Mock
//    private lateinit var addToCartUseCase: AddToCartUseCase
//
//    private lateinit var homeViewModel: HomeViewModel
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.openMocks(this)
//        homeViewModel = HomeViewModel(getProductsUseCase, addToCartUseCase)
//    }
//
//    @Test
//    fun testGetProductsSuccess() = runTest(testDispatcher) {
//        val products = fakeProducts()
//
//        `when`(getProductsUseCase.getProducts()).thenReturn(Success(products))
//
//        homeViewModel.getProducts()
// //        withContext(Dispatchers.Default) {
// //            delay(2.seconds)
// //        }
//        runCurrent()
//
//        val uiState = homeViewModel.homeUiState.value
//        println("UI State: $uiState")
//        println("Expected Products: $products")
//
//        assert(uiState.products == products) { "Expected products to be $products but was ${uiState.products}" }
//        assert(!uiState.isLoading) { "Expected isLoading to be false but was ${uiState.isLoading}" }
//    }
//
//    @Test
//    fun testGetProductsFailure() = runTest(testDispatcher) {
//        `when`(getProductsUseCase.getProducts()).thenReturn(Failure(Exception("Error")))
//
//        homeViewModel.getProducts()
//
//        assert(homeViewModel.homeUiState.value.messageResId != null)
//        assert(!homeViewModel.homeUiState.value.isLoading)
//    }
//
//    @Test
//    fun testAddProductToCart() = runTest(testDispatcher) {
//        val productId = 1
//
//        homeViewModel.addProductToCart(productId)
//
//        verify(addToCartUseCase).addToCart(productId)
//    }
//
//    private fun fakeProducts() = listOf(
//        Chair(
//            id = 1,
//            name = "Henriksdal",
//            price = Price(100.0, "kr"),
//            type = "chair",
//            imageUrl = "https://shop.static.ingka.ikea.com/PIAimages/0462849_PE608354_S4.JPG",
//            info = ChairInfo(
//                material = "wood",
//                color = "black",
//            ),
//        ),
//        Couch(
//            id = 1,
//            name = "Lidhult",
//            price = Price(1035.0, "kr"),
//            type = "couch",
//            imageUrl = "https://shop.static.ingka.ikea.com/PIAimages/0667779_PE714073_S4.JPG",
//            info = CouchInfo(
//                numberOfSeats = 4,
//                color = "beige",
//            ),
//        ),
//    )
// }
