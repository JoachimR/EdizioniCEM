package de.reiss.edizioni.note.edit

import android.arch.lifecycle.MutableLiveData
import de.reiss.edizioni.architecture.AsyncLoad
import de.reiss.edizioni.database.NoteItem
import de.reiss.edizioni.database.NoteItemDao
import de.reiss.edizioni.database.converter.Converter
import de.reiss.edizioni.logger.logWarn
import de.reiss.edizioni.model.Note
import de.reiss.edizioni.model.TheWordContent
import de.reiss.edizioni.util.extensions.withZeroDayTime
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class EditNoteRepository @Inject constructor(private val executor: Executor,
                                                  private val noteItemDao: NoteItemDao) {

    open fun loadNote(date: Date, result: MutableLiveData<AsyncLoad<Note?>>) {
        val oldData = result.value?.data
        result.postValue(AsyncLoad.loading(oldData))
        executor.execute {
            try {
                val noteItem = noteItemDao.byDate(date.withZeroDayTime())
                val note = Converter.noteItemToNote(noteItem) // can be null (new note)
                result.postValue(AsyncLoad.success(note))
            } catch (e: Exception) {
                logWarn(e) { "Error while loading note" }
                result.postValue(AsyncLoad.error(oldData))
            }
        }
    }

    open fun updateNote(date: Date,
                        text: String,
                        theWordContent: TheWordContent,
                        result: MutableLiveData<AsyncLoad<Void>>) {
        result.postValue(AsyncLoad.loading())
        executor.execute {
            try {
                if (text.trim().isEmpty()) {
                    noteItemDao.byDate(date.withZeroDayTime())?.let { noteItem ->
                        noteItemDao.delete(noteItem)
                    }
                } else {
                    noteItemDao.insertOrReplace(NoteItem(date.withZeroDayTime(), theWordContent, text))
                }
                result.postValue(AsyncLoad.success())
            } catch (e: Exception) {
                logWarn(e) { "Error while trying to update note" }
                result.postValue(AsyncLoad.error())
            }
        }
    }

}