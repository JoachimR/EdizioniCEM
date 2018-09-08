package de.reiss.edizioni.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompatDividers
import de.reiss.edizioni.R
import de.reiss.edizioni.util.extensions.isPlayServiceAvailable


class AppPreferencesFragment : PreferenceFragmentCompatDividers() {

    companion object {

        fun newInstance() = AppPreferencesFragment()

    }

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        try {
            return super.onCreateView(inflater, container, savedInstanceState)
        } finally {
            setDividerPreferences(PreferenceFragmentCompatDividers.DIVIDER_PADDING_CHILD
                    or PreferenceFragmentCompatDividers.DIVIDER_CATEGORY_AFTER_LAST
                    or PreferenceFragmentCompatDividers.DIVIDER_CATEGORY_BETWEEN)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreference(getString(R.string.pref_show_daily_notification_key)).apply {
            val playServiceAvailable = context.isPlayServiceAvailable()
            isVisible = playServiceAvailable
            setDefaultValue(playServiceAvailable)
        }
    }


}
