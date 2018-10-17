package de.reiss.edizioni.preferences

import android.os.Bundle
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat
import de.reiss.edizioni.R


class AppPreferencesFragment : PreferenceFragmentCompat() {

    companion object {

        fun newInstance() = AppPreferencesFragment()

    }

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}
