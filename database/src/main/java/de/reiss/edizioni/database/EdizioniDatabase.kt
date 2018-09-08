package de.reiss.edizioni.database

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import de.reiss.edizioni.database.daos.*
import de.reiss.edizioni.database.items.meta.AuthorItem
import de.reiss.edizioni.database.items.meta.InfoItem
import de.reiss.edizioni.database.items.meta.MetaItem
import de.reiss.edizioni.database.items.text.DevotionItem
import de.reiss.edizioni.database.items.text.TextItem

@Database(
        entities = [
            TextItem::class,
            DevotionItem::class,
            AuthorItem::class,
            MetaItem::class,
            InfoItem::class
        ],
        version = 1,
        exportSchema = false
)
@TypeConverters(Converters::class)
abstract class EdizioniDatabase : RoomDatabase() {

    companion object {

        fun create(application: Application): EdizioniDatabase {
            return Room.databaseBuilder(application, EdizioniDatabase::class.java, "EdizioniDatabase.db")
                    .allowMainThreadQueries() // for widgets
                    .build()
        }
    }

    abstract fun infoItemDao(): InfoItemDao

    abstract fun authorItemDao(): AuthorItemDao

    abstract fun metaItemDao(): MetaItemDao

    abstract fun metaItemDaoFull(): MetaItemDaoFull

    abstract fun textItemDao(): TextItemDao

    abstract fun devotionItemDao(): DevotionItemDao

    abstract fun textItemDaoFull(): TextItemDaoFull

}