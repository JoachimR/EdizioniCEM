package de.reiss.edizioni.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.preference.PreferenceManager
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import de.reiss.edizioni.R
import de.reiss.edizioni.events.FontSizeChanged
import de.reiss.edizioni.events.postMessageEvent
import de.reiss.edizioni.util.extensions.change
import de.reiss.edizioni.widget.triggerWidgetRefresh

class AppPreferences(val context: Context) : OnSharedPreferenceChangeListener {
    
    companion object {

        private const val LAST_USED_IMAGE_URL = "LAST_USED_IMAGE_URL"
        private const val LAST_USED_YEAR_FOR_ABOUT = "LAST_USED_YEAR_FOR_ABOUT"
        
    }

    val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    init {
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (sharedPreferences == preferences) {
            if (isWidgetPref(key)) {
                triggerWidgetRefresh()
            } else {
                if (key == str(R.string.pref_fontsize_key)) {
                    postMessageEvent(FontSizeChanged())
                }
            }
        }
    }

    private fun isWidgetPref(key: String): Boolean = (key == str(R.string.pref_widget_fontsize_key)
            || key == str(R.string.pref_widget_fontcolor_key)
            || key == str(R.string.pref_widget_backgroundcolor_key)
            || key == str(R.string.pref_widget_showdate_key)
            || key == str(R.string.pref_widget_centered_text_key))

    fun getLastUsedImageUrl(): String? {
        return preferences.getString(LAST_USED_IMAGE_URL, null)
    }

    fun setLastUsedImageUrl(imageUrl: String?) {
        preferences.change {
            putString(LAST_USED_IMAGE_URL, imageUrl)
        }
    }

    fun getLastUsedYearForAbout(): Int {
        return preferences.getInt(LAST_USED_YEAR_FOR_ABOUT, -1)
    }

    fun setLastUsedYearForYearInfo(year: Int) {
        preferences.change {
            putInt(LAST_USED_YEAR_FOR_ABOUT, year)
        }
    }

    fun fontSize() = prefInt(
            stringRes = R.string.pref_fontsize_key,
            default = Integer.parseInt(str(R.string.pref_fontsize_max)))

    fun widgetShowDate() = prefBoolean(R.string.pref_widget_showdate_key, true)

    fun widgetFontColor() = prefInt(R.string.pref_widget_fontcolor_key,
            ContextCompat.getColor(context, R.color.font_black))

    fun widgetFontSize() = prefInt(
            stringRes = R.string.pref_widget_fontsize_key,
            default = Integer.parseInt(str(R.string.pref_widget_fontsize_default))).toFloat()

    fun widgetCentered() = prefBoolean(R.string.pref_widget_centered_text_key, true)

    fun widgetBackground(): String = prefString(R.string.pref_widget_backgroundcolor_key,
            R.string.pref_widget_backgroundcolor_default)

    fun changeFontSize(newFontSize: Int) {
        val min = Integer.parseInt(str(R.string.pref_fontsize_min))
        val max = Integer.parseInt(str(R.string.pref_fontsize_max))
        val changeValue = if (newFontSize < min) min else if (newFontSize > max) max else newFontSize
        preferences.change {
            putInt(str(R.string.pref_fontsize_key), changeValue)
        }
    }

    private fun prefString(@StringRes stringRes: Int, @StringRes defaultStringRes: Int? = null) =
            preferences.getString(str(stringRes),
                    if (defaultStringRes != null) str(defaultStringRes) else null)

    private fun prefBoolean(@StringRes stringRes: Int, default: Boolean) =
            preferences.getBoolean(str(stringRes), default)

    private fun prefInt(@StringRes stringRes: Int, default: Int) =
            preferences.getInt(str(stringRes), default)

    private fun str(@StringRes stringRes: Int) = context.getString(stringRes)

}