package de.reiss.edizioni.preferences

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import de.reiss.edizioni.R


class AppPreferencesFragment : PreferenceFragmentCompat() {

    companion object {

        fun newInstance() = AppPreferencesFragment()

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}
