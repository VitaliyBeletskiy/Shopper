package vibe.shopper.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vibe.shopper.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopperTopAppBar(
    modifier: Modifier = Modifier,
    titleText: String? = null,
    onNavigateBack: (() -> Unit)? = null,
    onCartClick: (() -> Unit)? = null,
    onSearchClick: (() -> Unit)? = null,
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
                        contentDescription = stringResource(R.string.back),
                    )
                }
            }
        },
        actions = {
            onSearchClick?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(R.string.search),
                    )
                }
            }
            onCartClick?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = stringResource(R.string.cart),
                    )
                }
            }
        },
    )
}
