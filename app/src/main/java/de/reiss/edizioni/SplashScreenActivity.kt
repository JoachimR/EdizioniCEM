package de.reiss.edizioni

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.reiss.edizioni.main.MainActivity


class SplashScreenActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent =
                Intent(context, SplashScreenActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(MainActivity.createIntent(this))
        finish()
    }

}
