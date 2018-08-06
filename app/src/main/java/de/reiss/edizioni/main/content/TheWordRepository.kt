package de.reiss.edizioni.main.content

import android.arch.lifecycle.MutableLiveData
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.database.NoteItemDao
import de.reiss.edizioni.database.TheWordItemDao
import de.reiss.edizioni.database.converter.Converter
import de.reiss.edizioni.model.Note
import de.reiss.edizioni.model.TheWord
import de.reiss.edizioni.preferences.AppPreferences
import de.reiss.edizioni.util.extensions.withZeroDayTime
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class TheWordRepository @Inject constructor(private val executor: Executor,
                                                 private val theWordItemDao: TheWordItemDao,
                                                 private val noteItemDao: NoteItemDao,
                                                 private val appPreferences: AppPreferences) {

    open fun getTheWordFor(date: Date, result: MutableLiveData<AsyncLoad<TheWord>>) {

        val oldData = result.value?.data
        result.value = AsyncLoad.loading(oldData)

        executor.execute {
            //            val fromDatabase = theWordItemDao.byDate(bibleItem.id, date.withZeroDayTime())
//            if (fromDatabase == null) {
//                result.postValue(AsyncLoad.error(message = "Content not found"))
//            } else {
//                result.postValue(AsyncLoad.success(dbItemToTheWord(bibleItem.bible, fromDatabase)))
//            }
            result.postValue(AsyncLoad.error(message = "Content not found"))
        }
    }

    open fun getNoteFor(date: Date, result: MutableLiveData<AsyncLoad<Note>>) {

        val oldData = result.value?.data
        result.postValue(AsyncLoad.loading(oldData))

        executor.execute {
            val noteItem = noteItemDao.byDate(date.withZeroDayTime())
            result.postValue(AsyncLoad.success(Converter.noteItemToNote(noteItem)))
        }
    }

}