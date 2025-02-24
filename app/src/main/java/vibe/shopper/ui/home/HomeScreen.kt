package vibe.shopper.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column {
            Text(
                text = "Hello Android!",
                modifier = Modifier.padding(innerPadding)
            )
            Button(
                onClick = {
                    viewModel.getProducts()
                },
            ) {
                Text("Do!")
            }
        }
    }
}

