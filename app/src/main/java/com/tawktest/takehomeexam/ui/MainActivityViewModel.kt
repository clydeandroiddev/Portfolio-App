package com.tawktest.takehomeexam.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tawktest.takehomeexam.data.repositories.UserRepository
import com.tawktest.takehomeexam.util.ApiException
import com.tawktest.takehomeexam.util.Coroutines
import com.tawktest.takehomeexam.util.GlobalListener
import com.tawktest.takehomeexam.util.NoInternetException
import java.io.IOException
import java.time.LocalDateTime

class MainActivityViewModel (val repository: UserRepository) : ViewModel() {

    var globalListener : GlobalListener? = null

    fun lastSaveAt() = repository.lastSavedAt()

    val toLoad = MutableLiveData<Boolean>()

    fun fetchUserList(){
        val lastSavedAt = repository.lastSavedAt()
        if(lastSavedAt == null || repository.isFetchNeeded(LocalDateTime.parse(lastSavedAt))){
            toLoad.postValue(true)
            globalListener?.onStarted()

            Coroutines.main {
                try {
                    val response = repository.getGithubUserLists(0)
                    response.let {
                        globalListener?.onSuccess("")
                        repository.saveUserList(it)
                        return@main
                    }
                }catch (e : ApiException){
                    globalListener?.onFailure("Error",e.message!!)
                }catch (e : NoInternetException){
                    if(lastSavedAt == null){
                        globalListener?.onFailure("Warning", "Please make sure you have internet connection when first time running the app.")
                    }else{
                        globalListener?.onFailure("Error",e.message!!)
                    }
                }catch (e : IOException){
                    globalListener?.onFailure("Error",e.message!!)
                }
            }
        }
    }

}