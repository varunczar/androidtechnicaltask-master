package com.cmcmarkets.android.exercise.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmcmarkets.android.data.ProductModel
import com.cmcmarkets.android.exercise.R
import com.cmcmarkets.android.util.ViewModelFactory
import com.cmcmarkets.android.viewmodels.WatchListProductsViewModel
import com.cmcmarkets.android.views.adapters.ProductsAdapter
import com.cmcmarkets.api.products.WatchlistTO
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_watchlists.view.*
import kotlinx.android.synthetic.main.watchlist.view.*
import javax.inject.Inject

/**
 * This fragment houses the watchlist chips and product details for each watch list item
 */
class WatchListsFragment : BaseFragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    @Inject
    lateinit var mProductsAdapter : ProductsAdapter

    var mWatchListProductsViewModel: WatchListProductsViewModel? = null

    var mRecyclerView : RecyclerView? = null
    private var mSelector : ChipGroup? = null
    private var mLoader : ContentLoadingProgressBar? = null
    private var mView: View? = null

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment and initialise the viewmodel
        if(mView==null) {
            mWatchListProductsViewModel = ViewModelProvider(activity as FragmentActivity, mViewModelFactory).get(WatchListProductsViewModel::class.java)
            mView = inflater.inflate(R.layout.fragment_watchlists, container, false)
            mView?.apply {
                mRecyclerView = products
                mSelector = selector
                mLoader = loader
                mError = error
            }
        }
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView?.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = mProductsAdapter
        }
    }

    /**
     * This method observes all viewmodels and updates the fragment if data changes
     */
    override fun observeViewModels() {
        //Observe changes to the watchlist items
        mWatchListProductsViewModel?.mWatchListItems?.observe(this, Observer { watchListTOs ->
            watchListTOs?.let {
                hideErrorMessage()
                createWatchListItems(it)
            } ?: run { setErrorMessage(resources.getString(R.string.error_no_data))}
        })

        //Observe changes to the load status
        mWatchListProductsViewModel?.mIsLoading?.observe(this, Observer { isLoading ->
            if(isLoading) mLoader?.show() else mLoader?.hide()
        })

        //Observe for any errors and handle the error
        mWatchListProductsViewModel?.getErrorsIfAny()?.observe(this, Observer {
            handleError(it)
        })

        //Observe for the selection of the watchlist item and fetch and populate the products
        mWatchListProductsViewModel?.mSelectedWatchTO?.observe(this, Observer {
            it?.details?.productIds?.let {
                hideErrorMessage()
                mWatchListProductsViewModel?.fetchProductModelsForProducts(it)
            }
        })

        //Observe for availability of product models and display them
        mWatchListProductsViewModel?.mProductModels?.observe(this, Observer {
            it?.let {
                hideErrorMessage()
                mProductsAdapter.addItems(it as ArrayList<ProductModel>)
            }
        })
    }

    /**
     * This method creates watchlist items as selectable chips
     */
    private fun createWatchListItems(watchlistTOs: List<WatchlistTO>) {
        mSelector?.apply {
            removeAllViews()
            watchlistTOs.forEach {
                val chip = View.inflate(context, R.layout.watchlist_item, null) as Chip
                chip.setText(it.details.name)
                chip.setOnCheckedChangeListener { compoundButton, isChecked ->
                    mProductsAdapter.clearItems()
                    mWatchListProductsViewModel?.mSelectedWatchTO?.postValue(it)
                }
                addView(chip)
            }
        }
    }

    companion object {
        /**
         * This factory method to create a new instance of
         * this fragment
         *
         * @return A new instance of fragment WatchListsFragment.
         */
        @JvmStatic
        fun newInstance() =
                WatchListsFragment()
    }

}
