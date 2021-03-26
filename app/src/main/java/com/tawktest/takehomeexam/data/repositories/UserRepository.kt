package com.tawktest.takehomeexam.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawktest.takehomeexam.UserListData
import com.tawktest.takehomeexam.data.db.AppDatabase
import com.tawktest.takehomeexam.data.network.ApiCalls
import com.tawktest.takehomeexam.data.network.SafeApiRequest
import com.tawktest.takehomeexam.model.UserProfileData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val api : ApiCalls,
    private val db : AppDatabase
) : SafeApiRequest() {

    private val userlist = MutableLiveData<List<UserListData>>()

    init {
        userlist.observeForever{
            //Save user list to database
        }
    }


    suspend fun userlist(id : Int) : LiveData<List<UserListData>>{
        return withContext(Dispatchers.IO){
            fetchUserList(id)
            db.getUserListDao().getUserList()
        }
    }

    suspend fun userprofile(url : String) : UserProfileData{
        return  apiRequest { api.userprofile(url) }
    }

    suspend fun fetchUserList(id : Int){
        val response = apiRequest { api.userlist(id) }
        userlist.postValue(response)
    }

    suspend fun saveUserProfile(profile: UserProfileData) = db.getUserProfileDao().insertUserProfil(profile)

}