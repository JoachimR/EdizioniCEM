package de.reiss.edizioni.database.items.text

import android.arch.persistence.room.*

@Entity(
        indices = [
            Index(
                    value = arrayOf("text", text_item_id),
                    unique = true
            )
        ],
        foreignKeys = [
            ForeignKey(
                    entity = TextItem::class,
                    parentColumns = arrayOf("id"),
                    childColumns = arrayOf(text_item_id),
                    onDelete = ForeignKey.CASCADE
            )
        ]
)
data class DevotionItem(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val text: String,
        @ColumnInfo(name = text_item_id) val textItemId: Long
)
