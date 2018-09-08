package de.reiss.edizioni.database.daos


import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import de.reiss.edizioni.database.items.meta.InfoItem

@Dao
interface InfoItemDao {

    @Insert
    fun insert(item: InfoItem): Long

    @Insert
    fun insertAll(vararg items: InfoItem): LongArray

    @Query("DELETE FROM InfoItem")
    fun clear()

}
