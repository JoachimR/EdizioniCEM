package de.reiss.edizioni.main

import android.content.Context
import de.reiss.edizioni.formattedDate
import java.text.SimpleDateFormat
import java.util.*

data class HeaderCalendar(var date: Date) {

    fun formattedDate(context: Context): String = formattedDate(context, date.time)

    fun weekDay(): String = formatDate("EEEE")

    fun dayOfMonth(): String = formatDate("dd")

    fun month(): String = formatDate("MMM")

    fun year(): String = formatDate("yyyy")

    private fun formatDate(format: String) =
            SimpleDateFormat(format, Locale.ITALIAN).format(date)
}
