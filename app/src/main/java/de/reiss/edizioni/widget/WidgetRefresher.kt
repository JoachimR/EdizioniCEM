package de.reiss.edizioni.widget

import android.content.Context
import android.support.annotation.WorkerThread
import de.reiss.edizioni.R
import de.reiss.edizioni.database.BibleItemDao
import de.reiss.edizioni.database.TheWordItemDao
import de.reiss.edizioni.database.converter.Converter
import de.reiss.edizioni.formattedDate
import de.reiss.edizioni.model.TheWord
import de.reiss.edizioni.preferences.AppPreferences
import de.reiss.edizioni.util.extensions.withZeroDayTime
import java.util.*
import javax.inject.Inject

open class WidgetRefresher @Inject constructor(private val context: Context,
                                               private val theWordItemDao: TheWordItemDao,
                                               private val bibleItemDao: BibleItemDao,
                                               private val appPreferences: AppPreferences) {

    @WorkerThread
    fun retrieveWidgetText(): String =
            findTheWord(Date().withZeroDayTime()).let { theWord ->
                if (theWord == null) {
                    context.getString(R.string.no_content)
                } else {
                    widgetText(
                            context = context,
                            theWord = theWord,
                            includeDate = appPreferences.widgetShowDate()
                    )
                }
            }

    @WorkerThread
    private fun findTheWord(date: Date): TheWord? = null
//            theWordItemDao.byDate(bibleItem.id, date)?.let { theWordItem ->
//                return Converter.theWordItemToTheWord(bibleItem.bible, theWordItem)
//            }

    private fun widgetText(context: Context, theWord: TheWord, includeDate: Boolean): String =
            StringBuilder().apply {
                if (includeDate) {
                    append(formattedDate(context = context, time = theWord.date.time))
                    append("<br><br>")
                }

                if (theWord.content.intro1.isNotEmpty()) {
                    append(theWord.content.intro1)
                    append("<br><br>")
                }
                append(theWord.content.text1)
                append("<br>")
                append(theWord.content.ref1)
                append("<br><br>")

                if (theWord.content.intro2.isNotEmpty()) {
                    append(theWord.content.intro2)
                    append("<br><br>")
                }
                append(theWord.content.text2)
                append("<br>")
                append(theWord.content.ref2)
            }.toString()

}