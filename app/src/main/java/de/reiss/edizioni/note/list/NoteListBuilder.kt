package de.reiss.edizioni.note.list

import de.reiss.edizioni.model.Note
import de.reiss.edizioni.util.view.StableListItem

object NoteListBuilder {

    fun buildList(notes: List<Note>): List<StableListItem> = notes
            .sortedBy { it.date }
            .map { NoteListItem(it) }

}