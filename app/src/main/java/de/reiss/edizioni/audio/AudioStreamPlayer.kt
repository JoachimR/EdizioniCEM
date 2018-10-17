package de.reiss.edizioni.audio

interface AudioStreamPlayer {

    fun play(url: String, progress: Long?)

    fun pause(url: String)

    fun isPlaying(url: String): Boolean

    fun isSetToUrl(url: String): Boolean

    fun seekTo(progress: Long)

    fun seekRelative(toCurrentPosition: Long)

    fun currentProgress(): Long

    fun currentDuration(): Long

    fun shutdown()

}