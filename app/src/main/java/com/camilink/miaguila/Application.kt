package com.camilink.miaguila

import android.app.Application
import com.camilink.miaguila.injection.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule)
            androidContext(this@MyApplication)
        }
    }

}