package com.harrisonog.arcraiderscompanion.di

import android.content.Context
import com.harrisonog.arcraiderscompanion.data.local.preferences.SyncPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun provideSyncPreferences(
        @ApplicationContext context: Context
    ): SyncPreferences {
        return SyncPreferences(context)
    }
}
