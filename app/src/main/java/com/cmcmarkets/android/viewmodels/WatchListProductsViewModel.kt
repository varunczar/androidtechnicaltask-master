package com.cmcmarkets.android.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmcmarkets.android.data.PriceModel
import com.cmcmarkets.android.data.ProductModel
import com.cmcmarkets.android.repository.Repository
import com.cmcmarkets.api.products.WatchlistTO

/**
 * This viewmodel aids in communicating with the repository and obtaining data for watchlists and products
 */
class WatchListProductsViewModel(val mRepository: Repository) : ViewModel() {

    var mIsLoading = MutableLiveData<Boolean>()
    var mWatchListItems = MutableLiveData<List<WatchlistTO>>()
    var mSelectedWatchTO = MutableLiveData<WatchlistTO>()
    var mProductModels = MutableLiveData<List<ProductModel>>()
    var mPriceModel = MutableLiveData<PriceModel>()

    init {
        //Populate the watchlist items
        fetchWatchLists()
    }

    /**
     * This methods calls on the repository to fetch watch list items
     */
    fun fetchWatchLists() {
        mRepository.getWatchListItems(mIsLoading, mWatchListItems)
    }

    /**
     * This methods calls on the repository to fetch product details for a list of product ids
     */
    fun fetchProductModelsForProducts(productIds: List<Long>) {
        mRepository.getProducts(mIsLoading, productIds, mProductModels)
    }

    /**
     * This methods calls on the repository to fetch product price details per product
     */
    fun fetchPriceForProduct(position: Int, productId: Long) {
        mRepository.getProductPrice(position, productId, mPriceModel)
    }

    /**
     * This is called by any observer observing error events
     */
    fun getErrorsIfAny(): MutableLiveData<Throwable> {
        return mRepository.mThrowable
    }

}