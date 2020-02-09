package com.cmcmarkets.android.injection.modules

import com.cmcmarkets.android.CustomApp
import com.cmcmarkets.android.data.ConnectionLiveData
import com.cmcmarkets.android.views.adapters.ProductsAdapter
import com.cmcmarkets.api.internal.implementations.MockProductApi
import com.cmcmarkets.api.internal.implementations.MockSessionApi
import com.cmcmarkets.api.internal.implementations.MockWatchlistApi
import com.cmcmarkets.api.products.IProductApi
import com.cmcmarkets.api.products.IWatchlistApi
import com.cmcmarkets.api.session.ISessionApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    /**
     * This method provides the session api to fetch session data
     */
    @Provides
    @Singleton
    fun provideSession(): ISessionApi {
        return MockSessionApi()
    }

    /**
     * This method provides the watchlist api to fetch watchlist data
     */
    @Provides
    @Singleton
    fun provideWatchlist(): IWatchlistApi {
        return MockWatchlistApi()
    }

    /**
     * This method provides the products api to fetch productdetails and price data
     */
    @Provides
    @Singleton
    fun provideProducts(): IProductApi {
        return MockProductApi()
    }

    /**
     * This method provides an instance of the connection livedata which monitors network connectivity
     */
    @Provides
    @Singleton
    fun provideConnectionLiveData(application: CustomApp): ConnectionLiveData {
        return ConnectionLiveData(application)
    }

    /**
     * This method provides an instance of the products adapter that displays product details
     */
    @Provides
    @Singleton
    fun provideProductsAdapter(): ProductsAdapter {
        return ProductsAdapter()
    }

}