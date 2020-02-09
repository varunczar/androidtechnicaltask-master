package com.cmcmarkets.android.exercise.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.cmcmarkets.android.data.ConnectionLiveData
import com.cmcmarkets.android.data.ConnectivityState
import com.cmcmarkets.android.exercise.R
import com.cmcmarkets.android.views.ErrorView
import com.cmcmarkets.api.ApiError
import kotlinx.android.synthetic.main.error.*

/**
 * This base fragment contains common attributes and methods across all fragments
 */
abstract class BaseFragment : Fragment() {

    var mConnectionLiveData: ConnectionLiveData? = null

    var mError: ErrorView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mConnectionLiveData = ConnectionLiveData(context!!)
        //Observe changes in the connectivity
        observeDataConnectivity()
        //Observe all viewmodels
        observeViewModels()
    }

    /**
     * This method sets the generic error message based on the message string passed in
     */
    fun setErrorMessage(message: String) {
        mError?.apply {
            visibility = View.VISIBLE
            error_text?.text = message
        }
    }

    /**
     * This method hides the error message
     */
    fun hideErrorMessage() {
        mError?.apply {
            visibility = View.GONE
            error_text?.text = ""
        }
    }

    /**
     * This method sets the network specific error message based on the message string passed in
     */
    fun setNetworkError(message: String) {
        mError?.apply {
            icon_error.setImageDrawable(resources.getDrawable(R.drawable.ic_signal_wifi_off_black_24dp, null))
        }
        setErrorMessage(message)
    }

    /**
     * This method observes changes in the network connectivity and displays/hides an error to/from the user accordingly
     */
    fun observeDataConnectivity() {
        mConnectionLiveData?.observe(this, Observer {
            when (it) {
                ConnectivityState.UNSTABLE -> setNetworkError(resources.getString(R.string.error_network_losing))
                ConnectivityState.LOST -> setNetworkError(resources.getString(R.string.error_network_lost))
                else -> hideErrorMessage()
            }
        })
    }

    /**
     * This method handles the different kinds of errors that the app encounters
     */
    fun handleError(throwable: Throwable) {
        when (throwable) {
            ApiError.SessionUnrecognized -> setErrorMessage(resources.getString(R.string.error_session_unrecognised))
            ApiError.SessionExpired -> setErrorMessage(resources.getString(R.string.error_session_expired))
            ApiError.Disconnected -> setErrorMessage(resources.getString(R.string.error_session_disconnected))
            else -> setErrorMessage(resources.getString(R.string.error_generic))
        }
    }

    abstract fun observeViewModels()
}