package de.reiss.edizioni

import android.content.Context
import android.text.format.DateUtils.*

fun formattedDate(context: Context, time: Long): String =
        formatDateTime(context,
                time,
                FORMAT_SHOW_DATE or FORMAT_SHOW_YEAR or FORMAT_SHOW_WEEKDAY
            or  FORMAT_ABBREV_WEEKDAY
        )
