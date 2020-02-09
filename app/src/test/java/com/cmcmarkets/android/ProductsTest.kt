package com.cmcmarkets.android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.cmcmarkets.android.TestUtils.setupTests
import com.cmcmarkets.android.data.ProductModel
import com.cmcmarkets.android.repository.Repository
import com.cmcmarkets.api.products.*
import com.cmcmarkets.api.session.ISessionApi
import com.cmcmarkets.api.session.SessionTO
import io.reactivex.Single
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class ProductsTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var iSessionApi: ISessionApi

    @Mock
    lateinit var iWatchListApi: IWatchlistApi

    @Mock
    lateinit var iProductApi: IProductApi

    @InjectMocks
    lateinit var mRepository: Repository

    val mProductTO = ProductTO("testname", "testtype")
    val mProductDetailsTO = ProductDetailsTO("testcountry", "testtype",
            "subtype","currency",0.0,10)

    val sessionTO = SessionTO(true,"testtoken")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        setupTests()
    }

    @Test
    fun testProductTOSuccess() {

        val testSession = Single.just(sessionTO)
        val testProductTO : Single<ProductTO> = Single.just(mProductTO)
        val testProductDetailsTO : Single<ProductDetailsTO> = Single.just(mProductDetailsTO)

        `when`(iSessionApi.sessionTokenSingle()).thenReturn(testSession)
        `when`(iProductApi.productSingle(sessionTO.token,1)).thenReturn(testProductTO)
        `when`(iProductApi.productDetailsSingle(sessionTO.token,1)).thenReturn(testProductDetailsTO)
        val liveData = MutableLiveData<List<ProductModel>>()
        mRepository.getProducts(MutableLiveData(), arrayListOf(1), liveData)
        assert(liveData.value != null)
        assert(liveData.value!!.size != 0)
        assert(liveData.value!!.get(0).productDetailsTO != null)
    }

    @Test
    fun testProductTOFailure() {

        val testSession = Single.just(sessionTO)
        val testProductTO : Single<ProductTO> = Single.error(Throwable())
        val testProductDetailsTO : Single<ProductDetailsTO> = Single.error(Throwable())

        `when`(iSessionApi.sessionTokenSingle()).thenReturn(testSession)
        `when`(iProductApi.productSingle(sessionTO.token,1)).thenReturn(testProductTO)
        `when`(iProductApi.productDetailsSingle(sessionTO.token,1)).thenReturn(testProductDetailsTO)
        val liveData = MutableLiveData<List<ProductModel>>()
        mRepository.getProducts(MutableLiveData(), arrayListOf(1), liveData)
        assert(liveData.value == null)

    }

}