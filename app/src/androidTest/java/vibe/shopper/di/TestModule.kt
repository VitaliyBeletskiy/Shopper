package vibe.shopper.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import vibe.shopper.data.CartRepository
import vibe.shopper.data.FakeCartRepository
import vibe.shopper.data.FakeProductRepository
import vibe.shopper.data.ProductRepository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepoModule::class],
)
object TestModule {

    @Provides
    @Singleton
    fun provideFakeProductRepository(): ProductRepository = FakeProductRepository()

    @Provides
    @Singleton
    fun provideFakeCartRepository(): CartRepository = FakeCartRepository()
}
