package de.reiss.edizioni.events

import java.util.*

sealed class AppEventMessage

data class JsonDownloadRequested(val year: Calendar) : AppEventMessage()

data class ViewPagerMoveRequest(val position: Int) : AppEventMessage()

class FontSizeChanged : AppEventMessage()

class DatabaseRefreshed : AppEventMessage()

data class AudioStreamProgress(val url: String,
                               val progressInMs: Long,
                               val durationInMs: Long) : AppEventMessage()

data class AudioFilePlaybackChanged(val url: String) : AppEventMessage()