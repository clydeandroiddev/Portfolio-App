package com.tawktest.takehomeexam.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tawktest.takehomeexam.data.repositories.UserRepository


@Suppress("UNCHECKED_CAST")
class MainActivityViewModelFactory (val repository: UserRepository) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(repository) as T
    }
}