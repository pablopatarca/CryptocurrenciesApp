package app.crypto.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class NetworkInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("Network","Headers: ${request.headers}")
        Log.d("Network","URL: ${request.url}")
        val res = chain.proceed(request)
        Log.d("Network","Response: ${res.body}")
        return res
    }
}