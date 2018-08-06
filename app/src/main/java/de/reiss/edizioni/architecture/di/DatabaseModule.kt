package de.reiss.edizioni.architecture.di

import android.app.Application
import dagger.Module
import dagger.Provides
import de.reiss.edizioni.database.BibleItemDao
import de.reiss.edizioni.database.NoteItemDao
import de.reiss.edizioni.database.TheWordDatabase
import de.reiss.edizioni.database.TheWordItemDao

@Module
open class DatabaseModule(private val application: Application) {

    open fun getDatabase(): TheWordDatabase = TheWordDatabase.create(application)

    @Provides
    @ApplicationScope
    open fun theWordDatabase() = getDatabase()

    @Provides
    @ApplicationScope
    open fun theWordItemDao(): TheWordItemDao = getDatabase().theWordItemDao()

    @Provides
    @ApplicationScope
    open fun bibleItemDao(): BibleItemDao = getDatabase().bibleItemDao()

    @Provides
    @ApplicationScope
    open fun noteItemDao(): NoteItemDao = getDatabase().noteItemDao()

}
