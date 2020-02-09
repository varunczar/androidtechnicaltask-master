package com.cmcmarkets.android.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cmcmarkets.api.ApiError
import com.cmcmarkets.api.products.IProductApi
import com.cmcmarkets.api.products.IWatchlistApi
import com.cmcmarkets.api.products.WatchlistTO
import com.cmcmarkets.api.session.ISessionApi
import com.cmcmarkets.api.session.SessionTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
                                    watchListItems.postValue(success)
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
     * This method clears the compositedisposable
     *
     */
    fun clearCompositeDisposable() {
        mCompositeDisposable.clear()
    }
}