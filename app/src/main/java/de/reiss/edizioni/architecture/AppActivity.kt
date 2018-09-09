package de.reiss.edizioni.architecture

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

abstract class AppActivity : AppCompatActivity() {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
