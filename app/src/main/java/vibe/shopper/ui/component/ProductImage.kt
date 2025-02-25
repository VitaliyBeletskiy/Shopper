package vibe.shopper.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import vibe.shopper.R

@Composable
fun ProductImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    placeholderResId: Int = R.drawable.ic_loading_img,
    errorResId: Int = R.drawable.ic_broken_image,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = stringResource(R.string.product_image),
        placeholder = painterResource(placeholderResId),
        error = painterResource(errorResId),
        modifier = modifier.clip(MaterialTheme.shapes.small),
    )
}
