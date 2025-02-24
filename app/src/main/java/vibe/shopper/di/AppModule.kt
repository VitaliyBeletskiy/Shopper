package vibe.shopper.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vibe.shopper.data.ProductRepository
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
}
