package com.cmcmarkets.android.data

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import javax.inject.Inject

/**
 * This class helps in monitoring the current network state
 */
class ConnectionLiveData constructor(val context: Context) : LiveData<ConnectivityState>() {

    private var connectivityManager: ConnectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        updateConnection()
        when {
            //Registering the network callback to monitor connectivity
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager.registerDefaultNetworkCallback(getConnectivityManagerCallback())
            else -> {
                val builder = NetworkRequest.Builder()
                        .addTransportType(TRANSPORT_CELLULAR)
                        .addTransportType(TRANSPORT_WIFI)
                connectivityManager.registerNetworkCallback(builder.build(), getConnectivityManagerCallback())
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        //Unregister the network callback
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

    /**
     * This callback method monitors the network state and updates the live data accordingly
     */
    private fun getConnectivityManagerCallback(): ConnectivityManager.NetworkCallback {
        connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                //Called when the network connectivity is good
                postValue(ConnectivityState.CONNECTED)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                //Called when the network connectivity is patchy and is about to be disconnected
                postValue(ConnectivityState.UNSTABLE)
            }
            override fun onLost(network: Network?) {
                //Called when the network connectivity is lost
                postValue(ConnectivityState.LOST)
            }
        }
        return connectivityManagerCallback
    }

    /**
     * This method checks the current status on load and updates the live data accordingly
     */
    private fun updateConnection() {
        val activeNetwork = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        activeNetwork?.let {
            if(it.hasTransport (TRANSPORT_CELLULAR) || it.hasTransport(TRANSPORT_WIFI)) {
                //Connected fine
                postValue(ConnectivityState.CONNECTED)
            }
            else {
                //Connectivity lost
                postValue(ConnectivityState.LOST)
            }
        } ?: run { postValue(ConnectivityState.LOST) }
    }
}