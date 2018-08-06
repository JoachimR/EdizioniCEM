package de.reiss.edizioni.note.details

import android.arch.lifecycle.MutableLiveData
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.database.NoteItemDao
import de.reiss.edizioni.database.converter.Converter
import de.reiss.edizioni.logger.logWarn
import de.reiss.edizioni.model.Note
import de.reiss.edizioni.util.extensions.withZeroDayTime
import java.util.concurrent.Executor
import javax.inject.Inject

open class NoteDetailsRepository @Inject constructor(private val executor: Executor,
                                                     private val noteItemDao: NoteItemDao) {

    open fun loadNote(note: Note, result: MutableLiveData<AsyncLoad<Note>>) {
        val oldData = result.value?.data
        result.postValue(AsyncLoad.loading(oldData))
        executor.execute {
            try {
                val noteItem = noteItemDao.byDate(note.date.withZeroDayTime())
                val loadedNote = Converter.noteItemToNote(noteItem)

                if (loadedNote == null) {
                    logWarn { "note not found" }
                    result.postValue(AsyncLoad.error(oldData))
                } else {
                    result.postValue(AsyncLoad.success(loadedNote))
                }
            } catch (e: Exception) {
                logWarn(e) { "Error while loading note" }
                result.postValue(AsyncLoad.error(oldData))
            }
        }
    }

    open fun deleteNote(note: Note,
                        result: MutableLiveData<AsyncLoad<Void>>) {
        val oldData = result.value?.data
        result.postValue(AsyncLoad.loading(oldData))
        executor.execute {
            val noteItem = noteItemDao.byDate(note.date.withZeroDayTime())
            val deletedRows = if (noteItem == null) 0 else noteItemDao.delete(noteItem)
            result.postValue(
                    if (deletedRows > 0) {
                        AsyncLoad.success()
                    } else {
                        AsyncLoad.error()
                    }
            )
        }
    }

}