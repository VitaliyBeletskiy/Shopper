package vibe.shopper.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import vibe.shopper.ui.screen.cart.CartScreen
import vibe.shopper.ui.screen.cart.CartViewModel
import vibe.shopper.ui.screen.home.HomeScreen
import vibe.shopper.ui.screen.home.HomeViewModel
import vibe.shopper.ui.screen.product.ProductScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Product : Screen("product")
    data object Cart : Screen("cart")
}

@Composable
fun ShopperApp() {
    val navController = rememberNavController()
    val homeViewModel = hiltViewModel<HomeViewModel>()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToCart = { navController.navigate(Screen.Cart.route) },
                onProductClicked = { product ->
                    homeViewModel.onProductClicked(product)
                    navController.navigate(Screen.Product.route)
                },
            )
        }
        composable(Screen.Product.route) {
            ProductScreen(
                viewModel = homeViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCart = { navController.navigate(Screen.Cart.route) },
            )
        }
        composable(Screen.Cart.route) {
            val cartViewModel = hiltViewModel<CartViewModel>()
            CartScreen(
                viewModel = cartViewModel,
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
