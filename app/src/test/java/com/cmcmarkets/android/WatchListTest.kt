package com.cmcmarkets.android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.cmcmarkets.android.TestUtils.setupTests
import com.cmcmarkets.android.repository.Repository
import com.cmcmarkets.api.products.IProductApi
import com.cmcmarkets.api.products.IWatchlistApi
import com.cmcmarkets.api.products.WatchlistDetailsTO
import com.cmcmarkets.api.products.WatchlistTO
import com.cmcmarkets.api.session.ISessionApi
import com.cmcmarkets.api.session.SessionTO
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class WatchListTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var iSessionApi: ISessionApi

    @Mock
    lateinit var iWatchListApi: IWatchlistApi

    @Mock
    lateinit var iProductApi: IProductApi

    @InjectMocks
    lateinit var repository : Repository

    val watchListTO = WatchlistTO("testId", WatchlistDetailsTO("test", arrayListOf()))

    val sessionTO = SessionTO(true,"testtoken")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        setupTests()
    }

    @Test
    fun testWatchListTOSuccess() {

        val testSession = Single.just(sessionTO)
        val testWatchListTOs : Single<List<WatchlistTO>> = Single.just(arrayListOf(watchListTO))

        `when`(iSessionApi.sessionTokenSingle()).thenReturn(testSession)
        `when`(iWatchListApi.watchlistsSingle(sessionTO.token)).thenReturn(testWatchListTOs)
        val liveData = MutableLiveData<List<WatchlistTO>>()
        repository.getWatchListItems(MutableLiveData(),liveData)
        assert(liveData.value != null)
        assert(liveData.value?.size == 1)
        assert(liveData.value?.get(0)?.details != null)
        assert(liveData.value?.get(0)?.details?.productIds != null)

    }

    @Test
    fun testWatchListTOFailure() {

        val testSession : Single<SessionTO> = Single.error(Throwable())
        val testWatchListTOs : Single<List<WatchlistTO>> = Single.error(Throwable())

        `when`(iSessionApi.sessionTokenSingle()).thenReturn(testSession)
        `when`(iWatchListApi.watchlistsSingle(sessionTO.token)).thenReturn(testWatchListTOs)
        val liveData = MutableLiveData<List<WatchlistTO>>()
        repository.getWatchListItems(MutableLiveData(),liveData)
        assert(liveData.value == null)
    }


}