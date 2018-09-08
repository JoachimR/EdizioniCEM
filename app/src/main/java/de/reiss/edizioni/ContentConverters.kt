package de.reiss.edizioni

import android.content.Context
import de.reiss.edizioni.database.items.text.TextItemFull
import de.reiss.edizioni.model.DailyText


fun dbItemToDailyText(item: TextItemFull?): DailyText? =
        if (item == null) null else {
            DailyText(
                    item.textItem.date,
                    item.textItem.verse,
                    item.textItem.bibleRef,
                    item.devotionItems.map { it.text },
                    item.textItem.author)
        }

fun dailyTextToString(context: Context,
                      dailyText: DailyText,
                      includeDate: Boolean = true,
                      lineSeparator: String = "\n") =
        StringBuilder().apply {
            if (includeDate) {
                append(formattedDate(context, dailyText.date.time))
                append(lineSeparator)
                append(lineSeparator)
            }
            append(dailyText.verse)
            append(lineSeparator)
            append(dailyText.bibleRef)
            append(lineSeparator)
            append(lineSeparator)
            for (devotion in dailyText.devotions) {
                append(devotion)
                append(lineSeparator)
            }
            append(lineSeparator)
            append(dailyText.author)
        }.toString()