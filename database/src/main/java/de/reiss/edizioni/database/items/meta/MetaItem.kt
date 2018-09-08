package de.reiss.edizioni.database.items.meta

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.util.*


@Entity(
        indices = [
            Index(
                    value = arrayOf("year"),
                    unique = true
            )
        ]
)
data class MetaItem(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val year: Int,
        val title: String,
        val updated: Date,
        @ColumnInfo(name = "image_url") val imageUrl: String,
        @ColumnInfo(name = "audio_url") val audioUrl: String,
        val authorsTitle: String
)