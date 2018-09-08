package de.reiss.edizioni.downloader.list

import android.content.Context
import com.squareup.moshi.Moshi
import de.reiss.edizioni.downloader.R
import de.reiss.edizioni.logger.logWarn
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter
import java.lang.Exception

open class CalendarFromRawDownloader(private val context: Context,
                                     private val moshi: Moshi) {

    open fun loadCalendar(year: Int): EdizioniJson? {
        try {
            val jsonAdapter = moshi.adapter<EdizioniJson>(EdizioniJson::class.java)
            val json = getJsonFromRaw(context)
            return jsonAdapter.fromJson(json)
        } catch (e: Exception) {
            logWarn(e) { "Error when trying to load json" }
        }
        logWarn { "Could not download json successfully" }
        return null
    }

    private fun getJsonFromRaw(context: Context): String {
        val writer = StringWriter()
        val buffer = CharArray(1024)
        context.resources.openRawResource(R.raw.data2019_3).use { stream ->
            val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
            var n = reader.read(buffer)
            while (n != -1) {
                writer.write(buffer, 0, n)
                n = reader.read(buffer)
            }
        }

        return writer.toString()
    }
}