package de.reiss.edizioni.downloader.list

interface DownloadCalendarCallback {

    fun onCalendarDownloadFinished(year: Int, success: Boolean, data: EdizioniJson? = null)

}