package de.reiss.edizioni.database.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import de.reiss.edizioni.database.items.text.TextItem


@Dao
interface TextItemDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(item: TextItem): Long

    @Insert
    fun insertAll(vararg items: TextItem): LongArray

    @Query("DELETE FROM TextItem")
    fun clear()
}

