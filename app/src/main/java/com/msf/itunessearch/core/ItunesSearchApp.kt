package com.msf.itunessearch.core

import android.app.Application
import com.msf.itunessearch.di.ItunesSearchDi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ItunesSearchApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ItunesSearchApp)
            modules(ItunesSearchDi.module)
        }
    }
}
