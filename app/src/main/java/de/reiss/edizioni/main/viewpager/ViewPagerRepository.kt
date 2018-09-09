package de.reiss.edizioni.main.viewpager

import android.arch.lifecycle.MutableLiveData
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.download.DownloadAndStore
import de.reiss.edizioni.widget.triggerWidgetRefresh
import java.util.concurrent.Executor
import javax.inject.Inject

open class ViewPagerRepository @Inject constructor(private val executor: Executor,
                                                   private val downloadAndStore: DownloadAndStore) {

    open fun getItemsFor(year: Int,
                         result: MutableLiveData<AsyncLoad<Void>>) {

        // set value instead of post value on purpose
        // otherwise the fragment might invoke this twice
        result.value = AsyncLoad.loading()

        executor.execute {
            val databaseUpdated = downloadAndStore.executeFor(year)
            if (databaseUpdated) {
                triggerWidgetRefresh()
            }
            result.postValue(AsyncLoad.success())
        }
    }

}