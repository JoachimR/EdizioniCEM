package de.reiss.edizioni.main.viewpager

import android.arch.lifecycle.MutableLiveData
import android.support.annotation.WorkerThread
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.database.daos.*
import de.reiss.edizioni.database.items.meta.AuthorItem
import de.reiss.edizioni.database.items.meta.InfoItem
import de.reiss.edizioni.database.items.meta.MetaItem
import de.reiss.edizioni.database.items.text.DevotionItem
import de.reiss.edizioni.database.items.text.TextItem
import de.reiss.edizioni.database.items.text.TextItemFull
import de.reiss.edizioni.downloader.list.CalendarDownloader
import de.reiss.edizioni.downloader.list.CalendarFromRawDownloader
import de.reiss.edizioni.downloader.list.EdizioniJson
import de.reiss.edizioni.downloader.list.EdizioniJsonText
import de.reiss.edizioni.logger.logWarn
import de.reiss.edizioni.util.extensions.firstDayOfYear
import de.reiss.edizioni.util.extensions.lastDayOfYear
import de.reiss.edizioni.util.extensions.withZeroDayTime
import de.reiss.edizioni.widget.triggerWidgetRefresh
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class ViewPagerRepository @Inject constructor(private val executor: Executor,
                                                   private val calendarDownloader: CalendarDownloader,
                                                   private val calendarFromRawDownloader: CalendarFromRawDownloader,
                                                   private val textItemDao: TextItemDao,
                                                   private val textItemDaoFull: TextItemDaoFull,
                                                   private val devotionItemDao: DevotionItemDao,
                                                   private val metaItemDao: MetaItemDao,
                                                   private val infoItemDao: InfoItemDao,
                                                   private val authorItemDao: AuthorItemDao) {

    private val textKeyDateFormat = "yyyy-MM-dd"

    open fun getItemsFor(year: Int,
                         result: MutableLiveData<AsyncLoad<Void>>) {

        // set value instead of post value on purpose
        // otherwise the fragment might invoke this twice
        result.value = AsyncLoad.loading()

        executor.execute {

            val storedItems = readStoredItems(year)
            val expectedAmountOfDays = 365

            if (storedItems == null || storedItems.size < expectedAmountOfDays) {
                logWarn {
                    "Not enough items found (actual:${storedItems?.size ?: 0}, " +
                            "expected:$expectedAmountOfDays) for year $year. " +
                            "Will try to download and store items"
                }

                val databaseUpdated = downloadAndStoreItems(year)

                // always return success, we only tried update
                result.postValue(AsyncLoad.success())

                if (databaseUpdated) {
                    triggerWidgetRefresh()
                }
            } else {
                result.postValue(AsyncLoad.success())
            }
        }
    }

    private fun readStoredItems(year: Int): List<TextItemFull>? {
        val dateOfYear = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
        }.time
        return textItemDaoFull.forDateBetween(
                dateOfYear.firstDayOfYear().withZeroDayTime(),
                dateOfYear.lastDayOfYear()
        )
    }

    @WorkerThread
    private fun downloadAndStoreItems(year: Int): Boolean {
//        calendarFromRawDownloader.loadCalendar(year)?.let { edizioniJson ->
//            return downloadAndStore(edizioniJson)
//        }
        calendarDownloader.downloadCalendar(year)?.let { jsonData ->
            return downloadAndStore(jsonData)
        }
        return false
    }

    @WorkerThread
    private fun downloadAndStore(jsonData: EdizioniJson): Boolean {
        val insertSuccess = insertMeta(jsonData)
        if (insertSuccess) {
            insertTextItems(jsonData.texts)
            return true
        }
        return false
    }

    private fun insertMeta(jsonData: EdizioniJson): Boolean {
        val metaItemId = metaItemDao.insert(
                MetaItem(year = jsonData.meta.year,
                        title = jsonData.meta.title,
                        updated = jsonData.meta.updated,
                        imageUrl = jsonData.meta.imageUrl,
                        audioUrl = jsonData.meta.audioUrl,
                        authorsTitle = jsonData.meta.authors.title))

        if (metaItemId == -1L) {
            return false
        }

        for (name in jsonData.meta.authors.names) {
            authorItemDao.insert(
                    AuthorItem(name = name,
                            metaItemId = metaItemId))
        }
        for (info in jsonData.meta.info) {
            infoItemDao.insert(
                    InfoItem(semantics = info.semantics,
                            title = info.title,
                            text = info.text,
                            metaItemId = metaItemId))
        }
        return true
    }

    private fun insertTextItems(textDictionary: Map<String, EdizioniJsonText>) {
        val dateFormat = SimpleDateFormat(textKeyDateFormat, Locale.getDefault())
        for (key in textDictionary.keys) {
            val date: Date?
            try {
                date = dateFormat.parse(key)
                if (date != null) {
                    val text = textDictionary[key]
                    if (text != null) {
                        insertTextForDate(date, text)
                    }
                }
            } catch (exception: ParseException) {
                exception.printStackTrace()
                logWarn(exception) { "Could not parse date of text item" }
            }
        }
    }

    private fun insertTextForDate(date: Date, text: EdizioniJsonText) {
        val textItemId = textItemDao.insert(
                TextItem(date = date.withZeroDayTime(),
                        verse = text.verse,
                        bibleRef = text.bibleRef,
                        author = text.author))
        if (textItemId != -1L) {
            for (line in text.devotion) {
                devotionItemDao.insert(
                        DevotionItem(text = line,
                                textItemId = textItemId))
            }
        }
    }


}