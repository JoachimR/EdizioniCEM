package de.reiss.edizioni.database.converter;


import org.jetbrains.annotations.Nullable;

import de.reiss.edizioni.database.NoteItem;
import de.reiss.edizioni.database.TheWordItem;
import de.reiss.edizioni.model.Note;
import de.reiss.edizioni.model.TheWord;
import de.reiss.edizioni.model.TheWordContent;

public class Converter {

    @Nullable
    public static TheWord theWordItemToTheWord(String bible, @Nullable TheWordItem theWordItem) {
        if (theWordItem == null) {
            return null;
        }
        return new TheWord(
                bible, theWordItem.date, new TheWordContent(theWordItem.book1, theWordItem.chapter1,
                theWordItem.verse1, theWordItem.id1, theWordItem.intro1, theWordItem.text1,
                theWordItem.ref1, theWordItem.book2, theWordItem.chapter2, theWordItem.verse2,
                theWordItem.id2, theWordItem.intro2, theWordItem.text2, theWordItem.ref2));
    }

    @Nullable
    public static Note noteItemToNote(@Nullable NoteItem noteItem) {
        if (noteItem == null) {
            return null;
        }
        return new Note(noteItem.date, noteItem.note, new TheWordContent(
                noteItem.book1, noteItem.chapter1, noteItem.verse1, noteItem.id1, noteItem.intro1,
                noteItem.text1, noteItem.ref1, noteItem.book2, noteItem.chapter2, noteItem.verse2,
                noteItem.id2, noteItem.intro2, noteItem.text2, noteItem.ref2));
    }

}