package com.tawktest.takehomeexam.util

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.LiveData
import com.tawktest.takehomeexam.data.network.NetworkConnectionInterceptor
import com.tawktest.takehomeexam.model.ConnectionData
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


class ConnectionLiveData(val context: Context) : LiveData<ConnectionData>() {

    override fun onActive() {
        super.onActive()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkReceiver, filter)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(networkReceiver)
    }

    private val networkReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.extras != null) {
                if(isNetworkConnected()){
                    if(hasInternetConnection()){
                       postValue(ConnectionData(Constants.CONNECTIVITY_TYPE_3))
                    }else{
                        postValue(ConnectionData(Constants.CONNECTIVITY_TYPE_2))
                    }
                }else{
                    postValue(ConnectionData(Constants.CONNECTIVITY_TYPE_1))
                }
            }
        }
    }

    fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
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
            Log.d(NetworkConnectionInterceptor.TAG, "No network available!")
        }
        return false
    }
}