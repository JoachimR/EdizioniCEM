package de.reiss.edizioni.architecture.di

import dagger.Module
import dagger.Provides
import de.reiss.edizioni.note.export.FileProvider
import de.reiss.edizioni.note.export.NotesExporter

@Module
open class NotesExportModule {

    @Provides
    @ApplicationScope
    open fun fileProvider() = FileProvider()

    @Provides
    @ApplicationScope
    open fun notesExporter(fileProvider: FileProvider): NotesExporter = NotesExporter(fileProvider)

}