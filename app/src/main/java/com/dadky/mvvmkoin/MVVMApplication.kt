package com.dadky.mvvmkoin

import android.app.Application
import com.dadky.mvvmkoin.data.db.AppDatabase
import com.dadky.mvvmkoin.data.network.MyApi
import com.dadky.mvvmkoin.data.network.NetworkConnectionInterceptor
import com.dadky.mvvmkoin.data.preferences.PreferenceProvider
import com.dadky.mvvmkoin.data.repositories.QuotesRepository
import com.dadky.mvvmkoin.data.repositories.UserRepository
import com.dadky.mvvmkoin.ui.auth.AuthViewModelFactory
import com.dadky.mvvmkoin.ui.home.profile.ProfileViewModelFactory
import com.dadky.mvvmkoin.ui.home.quotes.QuotesViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*

class MVVMApplication: Application(),KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule((this@MVVMApplication)))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { UserRepository(instance(),instance()) }
        bind() from singleton { QuotesRepository(instance(),instance(),instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { ProfileViewModelFactory(instance()) }
        bind() from provider { QuotesViewModelFactory(instance()) }

    }
}