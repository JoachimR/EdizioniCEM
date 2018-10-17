package de.reiss.edizioni

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.support.v7.preference.PreferenceManager
import de.reiss.edizioni.architecture.di.ApplicationComponent
import de.reiss.edizioni.architecture.di.ContextModule
import de.reiss.edizioni.architecture.di.DaggerApplicationComponent
import de.reiss.edizioni.architecture.di.DatabaseModule
import de.reiss.edizioni.audio.AudioStreamPlayerService

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
        bindToAudioPlayService()
    }

    private fun initPrefs() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }

    open fun createComponent(): ApplicationComponent =
            DaggerApplicationComponent.builder()
                    .contextModule(ContextModule(this))
                    .databaseModule(DatabaseModule(this))
                    .build()


    private fun bindToAudioPlayService() {
        val playIntent = Intent(this, AudioStreamPlayerService::class.java)
        bindService(playIntent, object : ServiceConnection {

            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                if (service is AudioStreamPlayerService.LocalBinder) {
                    AudioStreamPlayerService.instance = service.service
                }
            }

            override fun onServiceDisconnected(name: ComponentName) {
                AudioStreamPlayerService.instance?.stopSelf()
            }
        }, Context.BIND_AUTO_CREATE)
    }

}
