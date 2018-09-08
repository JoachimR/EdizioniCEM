package de.reiss.edizioni.preferences

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import de.reiss.edizioni.App
import de.reiss.edizioni.R
import de.reiss.edizioni.SplashScreenActivity
import de.reiss.edizioni.architecture.AppActivity
import de.reiss.edizioni.util.extensions.replaceFragmentIn
import kotlinx.android.synthetic.main.preference_activity.*

class AppPreferencesActivity : AppActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {

        fun createIntent(context: Context): Intent =
                Intent(context, AppPreferencesActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preference_activity)
        setSupportActionBar(preferences_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (supportFragmentManager.findFragmentById(R.id.preferences_fragment_container) == null) {
            replaceFragmentIn(R.id.preferences_fragment_container,
                    AppPreferencesFragment.newInstance())

        }

        App.component.appPreferences.registerListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.pref_theme_key)) {
            restartApp()
        }
    }

    private fun restartApp() {
        startActivity(SplashScreenActivity.createIntent(this)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        supportFinishAfterTransition()
    }

}
