package com.tawktest.takehomeexam.util

interface GlobalListener {

    fun onStarted()
    fun onSuccess(data : String)
    fun onFailure(title : String, message : String)

}