package vibe.shopper.ui.screen.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vibe.shopper.R
import vibe.shopper.data.model.CartItem
import vibe.shopper.data.model.Chair
import vibe.shopper.data.model.ChairInfo
import vibe.shopper.data.model.Price
import vibe.shopper.ui.component.ConfirmationDialog
import vibe.shopper.ui.component.ProductImage
import vibe.shopper.ui.component.ShopperTopAppBar

@Composable
fun CartScreen(viewModel: CartViewModel, onNavigateBack: () -> Unit) {
    val uiState by viewModel.cartUiState.collectAsStateWithLifecycle()
    val confirmOnRemove = remember { mutableIntStateOf(-1) }

    when {
        confirmOnRemove.intValue > 0 -> {
            ConfirmationDialog(
                title = stringResource(R.string.remove_item),
                message = stringResource(R.string.are_you_sure_you_want_to_remove_this_item),
                onDismissRequest = { confirmOnRemove.intValue = -1 },
                onConfirm = {
                    viewModel.removeProductFromCart(confirmOnRemove.intValue)
                    confirmOnRemove.intValue = -1
                },
            )
        }
    }

    Scaffold(
        topBar = {
            ShopperTopAppBar(
                titleText = "Cart",
                onNavigateBack = onNavigateBack,
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        if (uiState.cartItems.isEmpty()) {
            EmptyCartView(modifier = Modifier.padding(innerPadding))
        } else {
            CartContent(
                cartItems = uiState.cartItems,
                totalPrice = uiState.totalPrice,
                modifier = Modifier.padding(innerPadding),
                onRemoveCartItem = { productId -> confirmOnRemove.intValue = productId },
                onProductQuantityChanged = { productId, quantity ->
                    viewModel.changeProductQuantity(
                        productId,
                        quantity,
                    )
                },
            )
        }
    }
}

@Composable
private fun CartContent(
    cartItems: List<CartItem>,
    totalPrice: String,
    modifier: Modifier = Modifier,
    onRemoveCartItem: (Int) -> Unit = {},
    onProductQuantityChanged: (Int, Int) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = stringResource(R.string.total_price, totalPrice),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.titleLarge,
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(cartItems) { cartItem ->
                if (cartItem.product == null) {
                    ProductNotAvailableCard(
                        productId = cartItem.id,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onRemoveCartItem = onRemoveCartItem,
                    )
                } else {
                    CartProductCard(
                        cartItem = cartItem,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        onRemoveCartItem = onRemoveCartItem,
                        onProductQuantityChanged = onProductQuantityChanged,
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductNotAvailableCard(
    productId: Int,
    modifier: Modifier = Modifier,
    onRemoveCartItem: (Int) -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.product_not_available),
                modifier = modifier
                    .padding(16.dp)
                    .weight(1f),
            )
            IconButton(
                onClick = {
                    onRemoveCartItem(productId)
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.remove_from_cart),
                )
            }
        }
    }
}

@Composable
private fun CartProductCard(
    cartItem: CartItem,
    modifier: Modifier = Modifier,
    onRemoveCartItem: (Int) -> Unit,
    onProductQuantityChanged: (Int, Int) -> Unit,
) {
    cartItem.product ?: return
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row {
                ProductImage(
                    imageUrl = cartItem.product.imageUrl,
                    modifier = Modifier.size(80.dp, 80.dp),
                )
                Text(
                    text = cartItem.product.name,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${cartItem.product.price.value} ${cartItem.product.price.currency}",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Row {
                QuantityStepper(
                    quantity = cartItem.quantity,
                    onQuantityChanged = { quantity ->
                        onProductQuantityChanged(
                            cartItem.id,
                            quantity,
                        )
                    },
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        onRemoveCartItem(cartItem.id)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.remove_from_cart),
                    )
                }
            }
        }
    }
}

@Composable
private fun QuantityStepper(
    quantity: Int,
    onQuantityChanged: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { if (quantity > 1) onQuantityChanged(quantity - 1) }) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_media_previous),
                contentDescription = "Decrease Quantity",
            )
        }
        Text(
            text = quantity.toString(),
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        IconButton(onClick = { onQuantityChanged(quantity + 1) }) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_media_next),
                contentDescription = "Increase Quantity",
            )
        }
    }
}

@Composable
private fun EmptyCartView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.cart_is_empty),
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Preview
@Composable
private fun ProductNotAvailableCardPreview() {
    ProductNotAvailableCard(productId = 1, onRemoveCartItem = {})
}

@Preview
@Composable
private fun CartProductCardPreview() {
    CartProductCard(
        cartItem = CartItem(
            id = 1,
            quantity = 2,
            product = Chair(
                id = 1,
                name = "Ektorp",
                price = Price(
                    value = 10.0,
                    currency = "sek",
                ),
                type = "chair",
                imageUrl = "https://www.ikea.com/se/sv/images/products/oestanoe-stol-svart-remmarn-moerkgra__1119282_pe873451_s5.jpg?f=xs",
                info = ChairInfo(
                    material = "plastic",
                    color = "white",
                ),
            ),
        ),
        onRemoveCartItem = {},
        onProductQuantityChanged = { _, _ -> },
    )
}

@Preview
@Composable
private fun QuantityStepperPreview() {
    QuantityStepper(
        quantity = 3,
        onQuantityChanged = {},
    )
}
