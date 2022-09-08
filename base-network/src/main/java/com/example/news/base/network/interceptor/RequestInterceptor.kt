package com.example.news.base.network.interceptor

import com.example.news.base.network.connection.ConnectionManager
import com.example.news.base.network.connection.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

class RequestInterceptor(
    private val connectionManager: ConnectionManager
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!connectionManager.isConnected()) {
            throw NoConnectivityException()
        }
        val request = chain.request().newBuilder().build()
        val response = chain.proceed(request)

        val statusCode = response.code
        if (statusCode < 200 || statusCode > 299 || response.body == null) {
            Timber.e("url: ${request.url.toUrl()} statusCode: $statusCode ")
        }

        return response
    }
}
