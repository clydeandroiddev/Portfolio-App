package com.tawktest.takehomeexam.ui.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tawktest.takehomeexam.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class UserListViewModelFactory(
    private val repository: UserRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserListViewModel(repository) as T
    }
}