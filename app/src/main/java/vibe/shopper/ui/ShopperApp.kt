package vibe.shopper.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import vibe.shopper.ui.screen.cart.CartScreen
import vibe.shopper.ui.screen.home.HomeScreen
import vibe.shopper.ui.screen.home.HomeViewModel

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Cart : Screen("cart")
}

@Composable
fun ShopperApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(viewModel) {
                navController.navigate(Screen.Cart.route)
            }
        }
        composable(Screen.Cart.route) {
            CartScreen()
        }
    }
}
