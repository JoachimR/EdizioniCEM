package de.reiss.edizioni.database.items.meta

import android.arch.persistence.room.*

@Entity(
        indices = [
            Index(
                    value = arrayOf("name", meta_item_id),
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
data class AuthorItem(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val name: String,
        @ColumnInfo(name = meta_item_id) val metaItemId: Long
)
