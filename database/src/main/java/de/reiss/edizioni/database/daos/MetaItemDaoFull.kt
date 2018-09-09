package de.reiss.edizioni.database.daos


import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import de.reiss.edizioni.database.items.meta.MetaItemFull

@Dao
interface MetaItemDaoFull {

    @Query("SELECT * FROM MetaItem WHERE MetaItem.year = :year")
    fun forYear(year: Int): MetaItemFull?

}
