package de.reiss.edizioni.audio

interface AudioStreamPlayerEventListener {

    fun onPlaybackChanged(url: String, isNowPlaying: Boolean)

    fun onPlayError()

}