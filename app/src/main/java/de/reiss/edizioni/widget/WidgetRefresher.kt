package de.reiss.edizioni.widget

import android.content.Context
import android.support.annotation.WorkerThread
import de.reiss.edizioni.R
import de.reiss.edizioni.dailyTextToString
import de.reiss.edizioni.database.daos.TextItemDaoFull
import de.reiss.edizioni.dbItemToDailyText
import de.reiss.edizioni.preferences.AppPreferences
import de.reiss.edizioni.util.extensions.withZeroDayTime
import java.util.*
import javax.inject.Inject

open class WidgetRefresher @Inject constructor(private val context: Context,
                                               private val textItemDaoFull: TextItemDaoFull,
                                               private val appPreferences: AppPreferences) {

    @WorkerThread
    fun retrieveWidgetText(): String =
            findDailyText(Date().withZeroDayTime()).let { dailyText ->
                if (dailyText == null) {
                    context.getString(R.string.no_content)
                } else {
                    dailyTextToString(
                            context = context,
                            dailyText = dailyText,
                            includeDate = appPreferences.widgetShowDate(),
                            lineSeparator = "<br>"
                    )
                }
            }

    @WorkerThread
    private fun findDailyText(date: Date) =
            dbItemToDailyText(textItemDaoFull.forDate(date.withZeroDayTime()))

}