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

   fun retrievedUserLists() = repository.retrieveUserList()




    suspend fun loadMoreUserList(lastID: Int) = repository.loadmMoreUserlist(lastID)


}