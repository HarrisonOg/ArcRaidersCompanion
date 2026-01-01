package com.harrisonog.arcraiderscompanion

import android.app.Application
import com.harrisonog.arcraiderscompanion.data.initialization.AppInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ArcRaidersApplication : Application() {

    @Inject
    lateinit var appInitializer: AppInitializer

    override fun onCreate() {
        super.onCreate()
        appInitializer.initialize()
    }
}