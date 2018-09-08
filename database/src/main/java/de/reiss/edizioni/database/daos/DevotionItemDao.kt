package de.reiss.edizioni.database.daos


import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import de.reiss.edizioni.database.items.text.DevotionItem

@Dao
interface DevotionItemDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(item: DevotionItem): Long

    @Insert
    fun insertAll(vararg items: DevotionItem): LongArray

    @Query("DELETE FROM DevotionItem")
    fun clear()

}