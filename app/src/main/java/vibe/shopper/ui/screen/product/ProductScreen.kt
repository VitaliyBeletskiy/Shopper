package vibe.shopper.ui.screen.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vibe.shopper.R
import vibe.shopper.data.model.Chair
import vibe.shopper.data.model.ChairInfo
import vibe.shopper.data.model.Couch
import vibe.shopper.data.model.Price
import vibe.shopper.data.model.Product
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
                cartItemCount = uiState.cartItemCount,
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
        ProductView(product, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
private fun ProductView(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        ProductImage(
            imageUrl = product.imageUrl,
            modifier = Modifier.fillMaxWidth(),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${product.price.value} ${product.price.currency}",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            Text(
                text = product.type,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(12.dp))

            when (product) {
                is Chair -> {
                    Text(
                        text = stringResource(R.string.color, product.info.color),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        text = stringResource(R.string.material, product.info.material),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }

                is Couch -> {
                    Text(
                        text = stringResource(R.string.color, product.info.color),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        text = stringResource(R.string.number_of_seats, product.info.numberOfSeats),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun AddToCartFab(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = { },
        text = { Text(text = stringResource(R.string.add_to_cart)) },
    )
}

@Composable
private fun NoProductFoundWarning(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(stringResource(R.string.no_product_found))
    }
}

@Preview
@Composable
private fun ProductViewPreview() {
    ProductView(
        product = Chair(
            id = 1,
            name = "Henriksdal",
            price = Price(100.0, "kr"),
            type = "chair",
            imageUrl = "https://www.ikea.com/se/sv/images/products/oestanoe-stol-svart-remmarn-moerkgra__1119282_pe873451_s5.jpg?f=xs",
            info = ChairInfo(
                material = "wood",
                color = "black",
            ),
        ),
    )
}
