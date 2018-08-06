package de.reiss.edizioni.main.viewpager

import android.arch.lifecycle.MutableLiveData
import android.support.annotation.WorkerThread
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.database.BibleItemDao
import de.reiss.edizioni.database.TheWordItem
import de.reiss.edizioni.database.TheWordItemDao
import de.reiss.edizioni.downloader.list.CalendarDownloader
import de.reiss.edizioni.downloader.list.CalendarFromRawDownloader
import de.reiss.edizioni.downloader.list.EdizioniJson
import de.reiss.edizioni.logger.logWarn
import de.reiss.edizioni.widget.triggerWidgetRefresh
import java.util.concurrent.Executor
import javax.inject.Inject

open class ViewPagerRepository @Inject constructor(private val executor: Executor,
                                                   private val calendarDownloader: CalendarDownloader,
                                                   private val calendarFromRawDownloader: CalendarFromRawDownloader,
                                                   private val theWordItemDao: TheWordItemDao,
                                                   private val bibleItemDao: BibleItemDao) {

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

    private fun readStoredItems(year: Int): List<TheWordItem>? {
        return emptyList()
    }

    @WorkerThread
    private fun downloadAndStoreItems(year: Int): Boolean {
        calendarFromRawDownloader.loadCalendar(year)?.let { edizioniJson ->
            return downloadAndStore(year, edizioniJson)
        }
//        calendarDownloader.downloadCalendar(year)?.let { edizioniJson ->
//            return downloadAndStore(year, edizioniJson)
//        }
        return false
    }

    @WorkerThread
    private fun downloadAndStore(year: Int, edizioniJson: EdizioniJson): Boolean {
        println(edizioniJson)
//
//        val parsed = JsonParser.parse(edizioniJson)
//        edizioniJson.
//        val items = parsed.map {
//            asDatabaseItem(bibleItemId, it)
//        }.toTypedArray()
//
//        val inserted = theWordItemDao.insertOrReplace(*items)
//        return inserted.isNotEmpty()
        return false
    }

//    private fun asDatabaseItem(bibleId: Int, text: EdizioniJsonText) =
//            TheWordItem().apply {
//                this.bibleId = bibleId
//                this.date = twd.date.withZeroDayTime()
//                this.book1 = twd.parol1.book
//                this.chapter1 = twd.parol1.chapter
//                this.verse1 = twd.parol1.verse
//                this.id1 = twd.parol1.id
//                this.intro1 = twd.parol1.intro
//                this.text1 = twd.parol1.text
//                this.ref1 = twd.parol1.ref
//                this.book2 = twd.parol2.book
//                this.chapter2 = twd.parol2.chapter
//                this.verse2 = twd.parol2.verse
//                this.id2 = twd.parol2.id
//                this.intro2 = twd.parol2.intro
//                this.text2 = twd.parol2.text
//                this.ref2 = twd.parol2.ref
//            }
}