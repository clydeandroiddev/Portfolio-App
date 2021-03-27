package com.tawktest.takehomeexam.ui.userlist

import androidx.lifecycle.ViewModel
import com.tawktest.takehomeexam.data.repositories.UserRepository
import com.tawktest.takehomeexam.util.ApiException
import com.tawktest.takehomeexam.util.Coroutines
import com.tawktest.takehomeexam.util.GlobalListener
import com.tawktest.takehomeexam.util.NoInternetException
import java.io.IOException
import java.time.LocalDateTime

class UserListViewModel(val repository: UserRepository) : ViewModel() {

    var globalListener : GlobalListener? = null

    suspend fun retrievedUserLists() = repository.retrieveUserList()


    fun fetchUserList(){
        val lastSavedAt = repository.lastSavedAt()
        if(lastSavedAt == null || repository.isFetchNeeded(LocalDateTime.parse(lastSavedAt))){
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
                    globalListener?.onFailure("Error",e.message!!)
                }catch (e : IOException){
                    globalListener?.onFailure("Error",e.message!!)
                }
            }
        }
    }

}