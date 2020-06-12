package com.robdir.githubusers.core

import android.net.ConnectivityManager
import javax.inject.Inject

class DeviceNetworkInfoProvider @Inject constructor(
    private val connectivityManager: ConnectivityManager?
) : NetworkInfoProvider {

    @Suppress("Deprecation")
    override fun isNetworkAvailable(): Boolean =
        connectivityManager?.activeNetworkInfo?.isConnected ?: false
}
