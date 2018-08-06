package de.reiss.edizioni.architecture.di

import android.content.Context
import dagger.Module
import dagger.Provides
import de.reiss.edizioni.preferences.AppPreferences

@Module(
        includes = [
            ContextModule::class
        ]
)
class PreferenceModule {

    @Provides
    @ApplicationScope
    fun appPreferences(context: Context): AppPreferences = AppPreferences(context)

}