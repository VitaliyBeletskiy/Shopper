package vibe.shopper.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vibe.shopper.data.ProductRepository
import vibe.shopper.data.ProductRepositoryImpl
import vibe.shopper.data.assets.AssetsDataSource
import vibe.shopper.data.assets.AssetsDataSourceImpl
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
        assetsDataSourceImpl: AssetsDataSourceImpl,
    ): AssetsDataSource
}
