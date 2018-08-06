package de.reiss.edizioni.note.list

import de.reiss.edizioni.model.Note

data class FilteredNotes constructor(val allItems: List<Note>,
                                     val filteredItems: List<Note>,
                                     val query: String) {

    constructor() : this(emptyList(), emptyList(), "")
}