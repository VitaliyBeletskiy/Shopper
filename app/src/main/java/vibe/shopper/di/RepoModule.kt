package vibe.shopper.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vibe.shopper.data.CartRepository
import vibe.shopper.data.CartRepositoryImpl
import vibe.shopper.data.ProductRepository
import vibe.shopper.data.ProductRepositoryImpl
import vibe.shopper.data.datasource.ProductsDataSource
import vibe.shopper.data.datasource.AssetsProductsDataSourceImpl
import vibe.shopper.data.datasource.CartDataSource
import vibe.shopper.data.datasource.SharedPrefsCartDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl,
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindAssetsDataSource(
        assetsProductsDataSourceImpl: AssetsProductsDataSourceImpl,
    ): ProductsDataSource

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl,
    ): CartRepository

    @Binds
    @Singleton
    abstract fun bindSharedPrefsDataSource(
        sharedPrefsDataSourceImpl: SharedPrefsCartDataSourceImpl,
    ): CartDataSource
}
