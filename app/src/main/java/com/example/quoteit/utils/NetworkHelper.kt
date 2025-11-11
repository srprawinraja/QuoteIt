package com.example.quoteit.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LifecycleObserver

class NetworkHelper(private val contextHelper: ContextHelper, private val onNetworkAvailable: () -> Unit) {
    var isRegistered: Boolean=false
    private val connectivityManager = contextHelper.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            // Called when network is available
            onNetworkAvailable()
        }

        override fun onLost(network: Network) {
            // Called when network is lost
        }
    }

    fun startMonitoring() {
        if(!isRegistered) {
            isRegistered=true
            val networkRequest = android.net.NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    fun stopMonitoring() {
        if(isRegistered){
            isRegistered=false
            connectivityManager.unregisterNetworkCallback(networkCallback)
            }
    }
    fun isNetworkAvailable(): Boolean {
        val cm = contextHelper.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    // Optional: check specifically for mobile data (cellular)
    fun isMobileDataActive(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}