package vibe.shopper.ui.screen.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vibe.shopper.R
import vibe.shopper.data.model.Chair
import vibe.shopper.data.model.ChairInfo
import vibe.shopper.data.model.Price
import vibe.shopper.data.model.Product
import vibe.shopper.ui.component.ProductImage
import vibe.shopper.ui.component.ShopperTopAppBar

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCart: () -> Unit,
    onProductClicked: (Product) -> Unit,
) {
    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()

    uiState.messageResId?.let { resId ->
        Toast.makeText(LocalContext.current, stringResource(resId), Toast.LENGTH_SHORT).show()
        viewModel.onMessageShown()
    }

    Scaffold(
        topBar = {
            ShopperTopAppBar(
                titleText = stringResource(R.string.products),
                onCartClick = onNavigateToCart,
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        ProductList(
            products = uiState.products,
            isRefreshing = uiState.isLoading,
            onRefresh = viewModel::getProducts,
            onProductClicked = onProductClicked,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductList(
    products: List<Product>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onProductClicked: (Product) -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(products) { product ->
                ProductListItem(product = product, onProductClicked = onProductClicked)
            }
        }
    }
}

@Composable
private fun ProductListItem(
    product: Product,
    onProductClicked: (Product) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onProductClicked.invoke(product) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        ) {
            ProductImage(imageUrl = product.imageUrl, modifier = Modifier.size(80.dp, 80.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = product.type,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "${product.price.value} ${product.price.currency}",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProductListItemPreview() {
    ProductListItem(
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
        onProductClicked = {},
    )
}
