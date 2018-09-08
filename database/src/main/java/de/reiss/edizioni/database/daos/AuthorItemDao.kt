package de.reiss.edizioni.database.daos


import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import de.reiss.edizioni.database.items.meta.AuthorItem

@Dao
interface AuthorItemDao {

    @Insert
    fun insert(item: AuthorItem): Long

    @Insert
    fun insertAll(vararg items: AuthorItem): LongArray

    @Query("DELETE FROM AuthorItem")
    fun clear()

}
