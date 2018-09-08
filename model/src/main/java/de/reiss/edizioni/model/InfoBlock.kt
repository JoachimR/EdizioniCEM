package de.reiss.edizioni.model

import android.os.Parcel
import android.os.Parcelable

data class InfoBlock(val semantics: String,
                     val title: String,
                     val text: String) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(semantics)
        writeString(title)
        writeString(text)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<InfoBlock> = object : Parcelable.Creator<InfoBlock> {
            override fun createFromParcel(source: Parcel): InfoBlock = InfoBlock(source)
            override fun newArray(size: Int): Array<InfoBlock?> = arrayOfNulls(size)
        }
    }
}