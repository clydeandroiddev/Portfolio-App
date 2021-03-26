package com.tawktest.takehomeexam

import android.app.Application
import com.tawktest.takehomeexam.data.db.AppDatabase
import com.tawktest.takehomeexam.data.network.ApiCalls
import com.tawktest.takehomeexam.data.network.NetworkConnectionInterceptor
import com.tawktest.takehomeexam.data.repositories.UserRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class MainApplication : Application(), KodeinAware {
    override val kodein: Kodein
        get() = Kodein.lazy {

            bind() from singleton { NetworkConnectionInterceptor(instance()) }
            bind() from singleton { ApiCalls(instance()) }
            bind() from singleton { AppDatabase(instance()) }
            bind() from singleton { UserRepository(instance(), instance()) }




        }
}