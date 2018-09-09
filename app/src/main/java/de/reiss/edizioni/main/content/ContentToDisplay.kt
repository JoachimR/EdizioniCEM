package de.reiss.edizioni.main.content

import de.reiss.edizioni.model.DailyText
import de.reiss.edizioni.model.YearInfo

data class ContentToDisplay(val dailyText: DailyText,
                            val yearInfo: YearInfo)