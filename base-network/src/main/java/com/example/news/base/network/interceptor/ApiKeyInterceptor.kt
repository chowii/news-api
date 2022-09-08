package com.example.news.base.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter(URL_API_KEY_QUERY_PARAM_KEY, apiKey)
            .build()
        val apiKeyRequest = request.newBuilder().url(url).build()
        return chain.proceed(apiKeyRequest)
    }

    companion object {
        private const val URL_API_KEY_QUERY_PARAM_KEY = "apiKey"
    }
}
