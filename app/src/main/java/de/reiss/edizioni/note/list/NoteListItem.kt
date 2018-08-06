package de.reiss.edizioni.note.list

import de.reiss.edizioni.model.Note
import de.reiss.edizioni.util.view.StableListItem

data class NoteListItem(val note: Note) : StableListItem() {

    override fun stableId() = hashCode().toLong()

}
