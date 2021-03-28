package com.tawktest.takehomeexam.ui.userprofile

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tawktest.takehomeexam.data.db.entities.UserProfileTable
import com.tawktest.takehomeexam.data.repositories.UserRepository
import com.tawktest.takehomeexam.ui.MainActivity
import com.tawktest.takehomeexam.util.*
import java.io.IOException


class UserProfileViewModel(val repository: UserRepository) : ViewModel() {

    var globalListener : GlobalListener? = null

    var user_id : Int? = null
    var notes : String? = null

    fun OnSaveNotes(view: View){
        globalListener?.onStarted()
        if(notes.isNullOrEmpty()){
            globalListener?.onFailure("Error", "Note is empty or invalid")
        }else if(user_id == null){
            globalListener?.onFailure("Error", "User id is invalid")
        } else{
            Coroutines.main {
                try{
                    val savenotes = repository.saveNotes(user_id!!, notes!!)
                    if(savenotes != 0){
                        (view.context as MainActivity).hideKeyboard()
                        globalListener?.onSuccess("Save note success")
                    }
                }catch (e: Exception){
                    globalListener?.onFailure("Error", e.message!!)
                }
            }
        }
    }

    suspend fun retrievedUserProfile(id: Int, url: String?) : MutableLiveData<UserProfileTable> {
        globalListener?.onStarted()

        if(id < 0){
            globalListener?.onFailure("Error", "User id is invalid")
        }

        val userprofile = MutableLiveData<UserProfileTable>()

        if(repository.retrieveUserProfile(id) != null){
            userprofile.value = repository.retrieveUserProfile(id)
            globalListener?.onSuccess("")
        }else{
            if(url.isNullOrEmpty()){
                globalListener?.onFailure("Error", "User data is empty")
            }else{
                try{
                    val response = repository.getGithubUserProfile(url)
                    response.let {
                        repository.saveUserProfile(it)
                        userprofile.value = it
                    }
                }catch (e: ApiException){
                    globalListener?.onFailure("Error", e.message!!)
                }catch (e: NoInternetException){
                    globalListener?.onFailure("Error", e.message!!)
                }catch (e: IOException){
                    globalListener?.onFailure("Error", e.message!!)
                }
            }
        }



        return userprofile
    }
}