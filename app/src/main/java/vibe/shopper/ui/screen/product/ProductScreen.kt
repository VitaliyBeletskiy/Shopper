package vibe.shopper.ui.screen.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vibe.shopper.R
import vibe.shopper.data.model.Chair
import vibe.shopper.data.model.Couch
import vibe.shopper.ui.component.ProductImage
import vibe.shopper.ui.component.ShopperTopAppBar
import vibe.shopper.ui.screen.home.HomeViewModel

@Composable
fun ProductScreen(
    viewModel: HomeViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit,
) {
    val uiState by viewModel.productUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            ShopperTopAppBar(
                onNavigateBack = onNavigateBack,
                onCartClick = onNavigateToCart,
            )
        },
        floatingActionButton = {
            uiState.product?.let { product ->
                AddToCartFab {
                    viewModel.addProductToCart(product.id)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        val product = uiState.product ?: run {
            NoProductFoundWarning(modifier = Modifier.padding(innerPadding))
            return@Scaffold
        }

        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.Top,
        ) {
            ProductImage(
                imageUrl = product.imageUrl,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = product.name,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = product.type,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Start),
                style = MaterialTheme.typography.bodyLarge,
            )

            when (product) {
                is Chair -> {
                    Text(
                        text = stringResource(R.string.color, product.info.color),
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.Start),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = stringResource(R.string.material, product.info.material),
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.Start),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                is Couch -> {
                    Text(
                        text = stringResource(R.string.color, product.info.color),
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.Start),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = stringResource(R.string.number_of_seats, product.info.numberOfSeats),
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.Start),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            Text(
                text = "${product.price.value} ${product.price.currency}",
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Start),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
fun AddToCartFab(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = { },
        text = { Text(text = stringResource(R.string.add_to_cart)) },
    )
}

@Composable
fun NoProductFoundWarning(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(stringResource(R.string.no_product_found))
    }
}
