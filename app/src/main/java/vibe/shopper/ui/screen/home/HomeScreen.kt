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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import vibe.shopper.R
import vibe.shopper.data.model.Product

@Composable
fun HomeScreen(viewModel: HomeViewModel, navigateToCart: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState.messageResId?.let { resId ->
        Toast.makeText(LocalContext.current, stringResource(resId), Toast.LENGTH_SHORT).show()
        viewModel.onMessageShown()
    }

    Scaffold(
        topBar = {
            HomeTopAppBar(
                onCartClick = navigateToCart,
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        if (uiState.isLoading) {
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.wrapContentSize(),
                )
            }
        } else {
            ProductList(
                products = uiState.products,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
fun ProductList(products: List<Product>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(count = products.size) { index ->
            ProductItem(product = products[index])
        }
    }
}

@Composable
fun ProductItem(product: Product, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        ) {
            ProductImage(imageUrl = product.imageUrl)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
                Text(text = product.type)
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = "${product.price.value} ${product.price.currency}")
            }
        }
    }
}

@Composable
fun ProductImage(imageUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = imageUrl,
        contentDescription = stringResource(R.string.product_image),
        placeholder = painterResource(R.drawable.ic_loading_img),
        error = painterResource(R.drawable.ic_broken_image),
        modifier = modifier
            .size(80.dp, 80.dp)
            .clip(MaterialTheme.shapes.small),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(modifier: Modifier = Modifier, onCartClick: () -> Unit) {
    TopAppBar(
        modifier = modifier.shadow(elevation = 5.dp),
        title = { Text(text = stringResource(R.string.products)) },
        actions = {
            IconButton(onClick = onCartClick) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Cart",
                )
            }
        },
    )
}
