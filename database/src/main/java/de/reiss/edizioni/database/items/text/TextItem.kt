package de.reiss.edizioni.database.items.text

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.util.*


@Entity(
        indices = [
            Index(
                    value = arrayOf("date"),
                    unique = true
            )
        ]
)
data class TextItem(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val date: Date,
        val verse: String,
        @ColumnInfo(name = "bible_ref") val bibleRef: String,
        val author: String
)
