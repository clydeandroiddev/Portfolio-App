package com.tawktest.takehomeexam.ui.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tawktest.takehomeexam.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class UserProfileViewModelFactory(val repository: UserRepository) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserProfileViewModel(repository) as T
    }


}