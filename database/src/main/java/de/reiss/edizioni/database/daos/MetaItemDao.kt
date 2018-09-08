package de.reiss.edizioni.database.daos


import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import de.reiss.edizioni.database.items.meta.MetaItem

@Dao
interface MetaItemDao {

    @Insert
    fun insert(item: MetaItem): Long

    @Query("DELETE FROM MetaItem")
    fun clear()

}
