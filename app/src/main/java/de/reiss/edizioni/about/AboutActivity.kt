package de.reiss.edizioni.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.reiss.edizioni.R
import de.reiss.edizioni.architecture.AppActivity
import de.reiss.edizioni.util.extensions.replaceFragmentIn
import kotlinx.android.synthetic.main.about_activity.*

class AboutActivity : AppActivity() {

    companion object {

        fun createIntent(context: Context): Intent =
                Intent(context, AboutActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_activity)
        setSupportActionBar(about_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (supportFragmentManager.findFragmentById(R.id.about_fragment_container) == null) {
            replaceFragmentIn(R.id.about_fragment_container,
                    AboutFragment.createInstance())

        }
    }
}