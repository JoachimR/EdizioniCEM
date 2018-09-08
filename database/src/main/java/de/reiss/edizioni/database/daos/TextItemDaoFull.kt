package de.reiss.edizioni.database.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import de.reiss.edizioni.database.items.text.TextItemFull
import java.util.*


@Dao
interface TextItemDaoFull {

    @Query("SELECT * FROM TextItem WHERE TextItem.date = :date")
    fun forDate(date: Date): TextItemFull?

    @Query("SELECT * FROM TextItem" +
            " WHERE TextItem.date BETWEEN :from AND :to")
    fun forDateBetween(from: Date, to: Date): List<TextItemFull>

}

