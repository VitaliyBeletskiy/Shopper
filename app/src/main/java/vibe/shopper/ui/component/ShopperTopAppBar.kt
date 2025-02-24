package vibe.shopper.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopperTopAppBar(
    modifier: Modifier = Modifier,
    titleText: String? = null,
    onNavigateBack: (() -> Unit)? = null,
    onCartClick: (() -> Unit)? = null,
) {
    TopAppBar(
        modifier = modifier.shadow(elevation = 5.dp),
        title = {
            titleText?.let { Text(text = it) }
        },
        navigationIcon = {
            onNavigateBack?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }
        },
        actions = {
            onCartClick?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Cart",
                    )
                }
            }
        },
    )
}
