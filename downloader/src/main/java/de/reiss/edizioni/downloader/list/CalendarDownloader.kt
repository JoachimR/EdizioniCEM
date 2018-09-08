package de.reiss.edizioni.downloader.list

import de.reiss.edizioni.logger.logWarn
import java.lang.Exception


open class CalendarDownloader(private val edizioniJsonService: EdizioniJsonService) {

    open fun downloadCalendar(year: Int): EdizioniJson? {
        try {
            val response = edizioniJsonService.calendar(year).execute()
            if (response?.isSuccessful == true) {
                return response.body()
            }
        } catch (e: Exception) {
            logWarn(e) { "Error when trying to load json" }
        }
        logWarn { "Could not download json successfully" }
        return null
    }

}