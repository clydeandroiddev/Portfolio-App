package com.tawktest.takehomeexam.data.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.tawktest.takehomeexam.util.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class NetworkConnectionInterceptor(context : Context) : Interceptor {

    private val applicationContext = context.applicationContext

    companion object {
        const val TAG = "NetworkConnectionInterceptor"
    }


    override fun intercept(chain: Interceptor.Chain): Response {
        //To check if connected to network and if has internet connection
        if(!isNetworkConnected())
            throw NoInternetException("Make sure you have internet connection")
        else
            if(!hasInternetConnection())
                throw NoInternetException("Slow internet connection")

        return chain.proceed(chain.request())
    }

    fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                connectivityManager?.let {
                    it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                        result = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            else -> false
                        }
                    }
                }
        return result
    }

    @SuppressLint("LongLogTag")
    fun hasInternetConnection(): Boolean {
        if (isNetworkConnected()) {
            try {
                // Connect to Google DNS to check for connection
                val timeoutMs = 10000
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)

                socket.connect(socketAddress, timeoutMs)
                socket.close()

                return true
            } catch (e: IOException) {
                return false
            }
        } else {
            Log.d(TAG, "No network available!")
        }
        return false
    }
}