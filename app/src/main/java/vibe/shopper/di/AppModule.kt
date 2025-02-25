package vibe.shopper.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import vibe.shopper.data.CartRepository
import vibe.shopper.data.ProductRepository
import vibe.shopper.domain.AddToCartUseCase
import vibe.shopper.domain.GetProductsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideGetLocationsUseCase(
        productRepository: ProductRepository,
    ): GetProductsUseCase = GetProductsUseCase(productRepository)

    @Provides
    @Singleton
    fun provideAddToCartUseCase(
        cartRepository: CartRepository,
    ): AddToCartUseCase = AddToCartUseCase(cartRepository)

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
}
