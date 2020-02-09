package com.cmcmarkets.android.repository

import androidx.lifecycle.MutableLiveData
import com.cmcmarkets.android.data.PriceModel
import com.cmcmarkets.android.data.ProductModel
import com.cmcmarkets.api.products.*
import com.cmcmarkets.api.session.ISessionApi
import com.cmcmarkets.api.session.SessionTO
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

/**
 * This main repository class handles connection to the server and updates the livedata instances with values
 */
class Repository @Inject constructor() {

    @Inject
    lateinit var iSessionApi: ISessionApi
    @Inject
    lateinit var iWatchlistApi: IWatchlistApi
    @Inject
    lateinit var iProductApi: IProductApi

    var mThrowable = MutableLiveData<Throwable>()

    val mCompositeDisposable = CompositeDisposable()

    /**
     * This method fetches watchlist for a session
     */
    fun getWatchListItems(isLoading: MutableLiveData<Boolean>,
                          watchListItems: MutableLiveData<List<WatchlistTO>>) {
        //Set load to true
        isLoading.postValue(true)
        mCompositeDisposable.add(
                //Fetch the session token
                iSessionApi.sessionTokenSingle()
                        .flatMap { sessionTO: SessionTO -> iWatchlistApi.watchlistsSingle(sessionTO.token) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { success ->
                                    isLoading.postValue(false)
                                    if(success.isNotEmpty()) {
                                        watchListItems.postValue(success)
                                    }
                                    else {
                                        mThrowable.postValue(Throwable("No data"))
                                    }
                                    clearCompositeDisposable()
                                }, {
                            isLoading.postValue(false)
                            mThrowable.postValue(it)
                            watchListItems.postValue(null);
                            clearCompositeDisposable()
                        })
        )
    }

    /**
     * This method fetches products and product details for a list of product Ids for a session
     */
    fun getProducts(isLoading : MutableLiveData<Boolean>,
                    productIds: List<Long>,
                    productModels : MutableLiveData<List<ProductModel>>) {
        isLoading.postValue(true)
        mCompositeDisposable.add(
                iSessionApi.sessionTokenSingle()
                        .flatMap { sessionTO: SessionTO ->  getProductModels(sessionTO.token, productIds) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {success -> isLoading.postValue(false)
                                    if(success.isNotEmpty()) {productModels.postValue(success)}
                                    else {
                                        mThrowable.postValue(Throwable("No data"))
                                    }
                                    clearCompositeDisposable()}, { isLoading.postValue(false)
                            mThrowable.postValue(it)
                            productModels.postValue(null); clearCompositeDisposable()})
        )
    }

    /**
     * This method fetches the product data and product details in parallel and returns a list of
     * product models
     */
    private fun getProductModels(sessionToken: String, list: List<Long>): Single<List<ProductModel>> {
        // Get an Observable of the list
        return Observable.fromIterable(list)
                // Get a Single<ResponseBody> for every Product ID
                .flatMapSingle {
                    Single.zip(
                            iProductApi.productSingle(sessionToken, it),
                            iProductApi.productDetailsSingle(sessionToken, it),
                            BiFunction<ProductTO, ProductDetailsTO, ProductModel>
                            { product, productDetails ->
                                ProductModel(it,product,productDetails)
                            })
                }
                // Put everything back on a list
                .toList()
    }

    /**
     * This method fetches the product prices data
     */
    fun getProductPrice(position : Int,productId : Long,  priceModel : MutableLiveData<PriceModel>) {
        mCompositeDisposable.add(
                iSessionApi.sessionTokenSingle()
                        .flatMap { sessionTO: SessionTO ->  iProductApi.productPrices(sessionTO.token, productId).firstOrError() }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {success -> priceModel.postValue(PriceModel(position, productId, success)); clearCompositeDisposable()}, {
                            mThrowable.postValue(it)
                            priceModel.postValue(null); clearCompositeDisposable()})
        )
    }

    /**
     * This method clears the compositedisposable
     *
     */
    fun clearCompositeDisposable() {
        mCompositeDisposable.clear()
    }
}