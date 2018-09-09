package de.reiss.edizioni.events

import java.util.*

sealed class AppEventMessage

data class JsonDownloadRequested(val year: Calendar) : AppEventMessage()

data class ViewPagerMoveRequest(val position: Int) : AppEventMessage()

data class ChangeDateDisplayRequest(val position: Int, val date: Date, val imageUrl: String) : AppEventMessage()

class FontSizeChanged : AppEventMessage()

class DatabaseRefreshed : AppEventMessage()
