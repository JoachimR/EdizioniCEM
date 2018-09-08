package de.reiss.edizioni

import android.app.Application
import android.support.v7.preference.PreferenceManager
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.stetho.Stetho
import de.reiss.edizioni.architecture.di.ApplicationComponent
import de.reiss.edizioni.architecture.di.ContextModule
import de.reiss.edizioni.architecture.di.DaggerApplicationComponent
import de.reiss.edizioni.architecture.di.DatabaseModule
import de.reiss.edizioni.notification.NotificationService
import io.fabric.sdk.android.Fabric

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
        Stetho.initializeWithDefaults(this);
        Fabric.with(this, Crashlytics.Builder()
                .core(CrashlyticsCore.Builder()
                        .disabled(BuildConfig.DEBUG)
                        .build())
                .build())
        NotificationService.schedule(this)
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
