package de.reiss.edizioni

import android.app.Application
import android.support.v7.preference.PreferenceManager
import de.reiss.edizioni.architecture.di.ApplicationComponent
import de.reiss.edizioni.architecture.di.ContextModule
import de.reiss.edizioni.architecture.di.DaggerApplicationComponent
import de.reiss.edizioni.architecture.di.DatabaseModule

open class App : Application() {

    companion object {

        @JvmStatic
        lateinit var component: ApplicationComponent

    }

    override fun onCreate() {
        super.onCreate()
        component = createComponent()
        initApp()
    }

    open fun initApp() {
        initPrefs()
    }

    private fun initPrefs() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }

    open fun createComponent(): ApplicationComponent =
            DaggerApplicationComponent.builder()
                    .contextModule(ContextModule(this))
                    .databaseModule(DatabaseModule(this))
                    .build()

}
