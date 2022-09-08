package com.example.news.base.network.connection

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ConnectionManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val connectivityManager: ConnectivityManager? = ContextCompat.getSystemService(
        context.applicationContext,
        ConnectivityManager::class.java
    )

    /**
     * Gets whether there is an internet connection
     *
     * @return [ConnectionType]
     */
    private val connectionType: ConnectionType
        get() {
            val networkInfo = connectivityManager?.activeNetworkInfo

            return when {
                networkInfo == null || !networkInfo.isConnected -> ConnectionType.NONE
                else -> ConnectionType.OTHER
            }
        }

    internal fun isConnected(): Boolean {
        return connectionType != ConnectionType.NONE
    }

    fun isNoConnectivityException(throwable: Throwable?): Boolean {
        return throwable is NoConnectivityException
    }
}
