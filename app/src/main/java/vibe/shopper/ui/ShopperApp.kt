package vibe.shopper.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import vibe.shopper.ui.home.HomeScreen
import vibe.shopper.ui.home.HomeViewModel

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Cart : Screen("cart")
}

@Composable
fun ShopperApp() {
    NavHost(
        navController = rememberNavController(),
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(viewModel)
        }
    }
}
