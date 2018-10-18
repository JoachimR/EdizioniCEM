package de.reiss.edizioni.about

import android.arch.lifecycle.MutableLiveData
import android.support.annotation.WorkerThread
import de.reiss.edizioni.about.model.About
import de.reiss.edizioni.about.model.SemanticsItem
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.database.daos.MetaItemDaoFull
import de.reiss.edizioni.preferences.AppPreferences
import de.reiss.edizioni.util.extensions.extractYear
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class AboutRepository @Inject constructor(private val executor: Executor,
                                               private val metaItemDaoFull: MetaItemDaoFull,
                                               private val appPreferences: AppPreferences) {

    open fun getAbout(result: MutableLiveData<AsyncLoad<About>>) {

        val oldData = result.value?.data
        result.value = AsyncLoad.loading(oldData)

        executor.execute {
            val about = tryCreateAbout()
            if (about != null) {
                result.postValue(AsyncLoad.success(about))
            } else {
                result.postValue(AsyncLoad.error())
            }
        }
    }

    @WorkerThread
    private fun tryCreateAbout(): About? {
        val currentYear = Date().extractYear()
        val currentAbout = aboutForYear(currentYear)
        if (currentAbout != null) {
            return currentAbout
        }
        val lastUsedYear = appPreferences.getLastUsedYearForAbout()
        if (lastUsedYear != -1) {
            return aboutForYear(lastUsedYear)
        }
        return null
    }

    @WorkerThread
    private fun aboutForYear(year: Int): About? {
        val metaItemFull = metaItemDaoFull.forYear(year)
        return if (metaItemFull == null) null else {
            About(year = year,
                    title = metaItemFull.metaItem.title,
                    semanticsItems = metaItemFull.infoItems.map {
                        SemanticsItem(semantics = it.semantics,
                                title = it.title,
                                text = it.text)
                    },
                    authors = metaItemFull.authorItems.map {
                        it.name
                    })
        }
    }

}