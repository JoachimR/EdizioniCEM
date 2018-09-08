package de.reiss.edizioni.main.content

import android.arch.lifecycle.MutableLiveData
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.database.daos.TextItemDao
import de.reiss.edizioni.database.daos.TextItemDaoFull
import de.reiss.edizioni.dbItemToDailyText
import de.reiss.edizioni.model.DailyText
import de.reiss.edizioni.preferences.AppPreferences
import de.reiss.edizioni.util.extensions.withZeroDayTime
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class DailyTextRepository @Inject constructor(private val executor: Executor,
                                                   private val textItemDao: TextItemDao,
                                                   private val textItemDaoFull: TextItemDaoFull,
                                                   private val appPreferences: AppPreferences) {

    open fun getDailyTextFor(date: Date, result: MutableLiveData<AsyncLoad<DailyText>>) {

        val oldData = result.value?.data
        result.value = AsyncLoad.loading(oldData)

        executor.execute {
            val item = textItemDaoFull.forDate(date.withZeroDayTime())
            if (item == null) {
                result.postValue(AsyncLoad.error(message = "Content not found"))
            } else {
                val newData = dbItemToDailyText(item)
                result.postValue(AsyncLoad.success(newData))
            }
        }
    }

}