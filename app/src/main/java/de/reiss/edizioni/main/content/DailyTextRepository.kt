package de.reiss.edizioni.main.content

import android.arch.lifecycle.MutableLiveData
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.database.daos.MetaItemDaoFull
import de.reiss.edizioni.database.daos.TextItemDao
import de.reiss.edizioni.database.daos.TextItemDaoFull
import de.reiss.edizioni.dbItemToDailyText
import de.reiss.edizioni.model.DailyText
import de.reiss.edizioni.model.YearInfo
import de.reiss.edizioni.preferences.AppPreferences
import de.reiss.edizioni.util.extensions.extractYear
import de.reiss.edizioni.util.extensions.withZeroDayTime
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class DailyTextRepository @Inject constructor(private val executor: Executor,
                                                   private val textItemDao: TextItemDao,
                                                   private val textItemDaoFull: TextItemDaoFull,
                                                   private val metaItemDaoFull: MetaItemDaoFull,
                                                   private val appPreferences: AppPreferences) {

    open fun getContentToDisplay(date: Date, result: MutableLiveData<AsyncLoad<ContentToDisplay>>) {

        val oldData = result.value?.data
        result.value = AsyncLoad.loading(oldData)

        executor.execute {
            val dailyText = findDailyText(date)
            if (dailyText != null) {
                val yearInfo = findYearInfo(date, oldData)
                if (yearInfo != null) {
                    val newData = ContentToDisplay(dailyText = dailyText, yearInfo = yearInfo)
                    result.postValue(AsyncLoad.success(newData))
                    return@execute
                }
            }
            result.postValue(AsyncLoad.error(message = "Content not found"))
        }
    }

    private fun findDailyText(date: Date): DailyText? {
        val item = textItemDaoFull.forDate(date.withZeroDayTime())
        return if (item != null) dbItemToDailyText(item) else null
    }

    private fun findYearInfo(date: Date,
                             oldData: ContentToDisplay?): YearInfo? {
        val year = date.extractYear()
        if (year != oldData?.yearInfo?.year) {
            val metaItemFull = metaItemDaoFull.forYear(year)
            if (metaItemFull != null) {
                return YearInfo(year = year,
                        imageUrl = metaItemFull.metaItem.imageUrl,
                        audioUrl = metaItemFull.metaItem.audioUrl,
                        updated = metaItemFull.metaItem.updated,
                        authors = metaItemFull.authorItems.map { it.name })
            }
        }
        return oldData?.yearInfo
    }

}