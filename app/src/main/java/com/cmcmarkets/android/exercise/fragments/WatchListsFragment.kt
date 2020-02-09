package com.cmcmarkets.android.exercise.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cmcmarkets.android.exercise.R
import dagger.android.support.AndroidSupportInjection

/**
 * This fragment houses the watchlist chips and product details for each watch list item
 */
class WatchListsFragment : Fragment() {

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_watchlists, container, false)
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
