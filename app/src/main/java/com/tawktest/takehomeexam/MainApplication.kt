package com.tawktest.takehomeexam

import android.app.Application
import com.tawktest.takehomeexam.data.db.AppDatabase
import com.tawktest.takehomeexam.data.network.ApiCalls
import com.tawktest.takehomeexam.data.network.NetworkConnectionInterceptor
import com.tawktest.takehomeexam.data.preferences.PreferenceProvider
import com.tawktest.takehomeexam.data.repositories.UserRepository
import com.tawktest.takehomeexam.ui.userlist.UserListViewModelFactory
import com.tawktest.takehomeexam.ui.userprofile.UserProfileViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MainApplication : Application(), KodeinAware {
    override val kodein: Kodein
        get() = Kodein.lazy {
            import(androidXModule(this@MainApplication))

            bind() from singleton { NetworkConnectionInterceptor(instance()) }
            bind() from singleton { ApiCalls(instance()) }
            bind() from singleton { AppDatabase(instance()) }
            bind() from singleton { PreferenceProvider(instance()) }
            bind() from singleton { UserRepository(instance(), instance(),instance()) }
            bind() from provider { UserListViewModelFactory(instance()) }
            bind() from provider { UserProfileViewModelFactory(instance())}




        }
}