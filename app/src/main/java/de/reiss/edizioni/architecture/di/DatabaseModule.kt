package de.reiss.edizioni.architecture.di

import android.app.Application
import dagger.Module
import dagger.Provides
import de.reiss.edizioni.database.EdizioniDatabase
import de.reiss.edizioni.database.daos.*

@Module
open class DatabaseModule(private val application: Application) {

    open fun db(): EdizioniDatabase = EdizioniDatabase.create(application)

    @Provides
    @ApplicationScope
    open fun edizioniDatabase() = db()

    @Provides
    @ApplicationScope
    open fun textItemDao(): TextItemDao =
            db().textItemDao()

    @Provides
    @ApplicationScope
    open fun textItemDaoFull(): TextItemDaoFull =
            db().textItemDaoFull()

    @Provides
    @ApplicationScope
    open fun metaItemDao(): MetaItemDao =
            db().metaItemDao()

    @Provides
    @ApplicationScope
    open fun metaItemDaoFull(): MetaItemDaoFull =
            db().metaItemDaoFull()

    @Provides
    @ApplicationScope
    open fun infoItemDao(): InfoItemDao =
            db().infoItemDao()

    @Provides
    @ApplicationScope
    open fun authorItemDao(): AuthorItemDao =
            db().authorItemDao()

    @Provides
    @ApplicationScope
    open fun devotionItemDao(): DevotionItemDao =
            db().devotionItemDao()

}
