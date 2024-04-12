package com.xubobo.composedemo

import android.app.Application
import timber.log.Timber

class ComposeApp : Application() {

    private val FORCE_LOG = false

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }


    private fun initTimber() {
        if (BuildConfig.DEBUG || FORCE_LOG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
