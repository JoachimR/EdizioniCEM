package de.reiss.edizioni.note.list

import de.reiss.edizioni.model.Note

interface NoteClickListener {

    fun onNoteClicked(note: Note)

}