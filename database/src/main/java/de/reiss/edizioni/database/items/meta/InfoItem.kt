package de.reiss.edizioni.database.items.meta

import android.arch.persistence.room.*


@Entity(
        indices = [
            Index(
                    value = arrayOf("semantics", meta_item_id),
                    unique = true
            )
        ],
        foreignKeys = [
            ForeignKey(
                    entity = MetaItem::class,
                    parentColumns = arrayOf("id"),
                    childColumns = arrayOf(meta_item_id),
                    onDelete = ForeignKey.CASCADE
            )
        ]
)
data class InfoItem(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val semantics: String,
        val title: String,
        val text: String,
        @ColumnInfo(name = meta_item_id) val metaItemId: Long
)