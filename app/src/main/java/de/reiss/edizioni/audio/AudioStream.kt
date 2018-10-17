package de.reiss.edizioni.audio

import android.os.Parcel
import android.os.Parcelable

data class AudioStream(val url: String,
                       val dayPosition: Int,
                       var progress: Long,
                       var duration: Long) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readInt(),
            source.readLong(),
            source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(url)
        writeInt(dayPosition)
        writeLong(progress)
        writeLong(duration)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<AudioStream> = object : Parcelable.Creator<AudioStream> {
            override fun createFromParcel(source: Parcel): AudioStream = AudioStream(source)
            override fun newArray(size: Int): Array<AudioStream?> = arrayOfNulls(size)
        }
    }
}