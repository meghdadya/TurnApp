package com.sample.turnapp

import android.app.Application
import com.sample.turnapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class TurnAppApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin { androidContext(this@TurnAppApplication) }
    }
}