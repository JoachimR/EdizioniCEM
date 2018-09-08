package de.reiss.edizioni.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class YearInfo(
        val year: Int,
        val imageUrl: String,
        val audioUrl: String,
        val updated: Date,
        val authors: List<String>) : Parcelable {

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readSerializable() as Date,
            source.createStringArrayList()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(year)
        writeString(imageUrl)
        writeString(audioUrl)
        writeSerializable(updated)
        writeStringList(authors)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<YearInfo> = object : Parcelable.Creator<YearInfo> {
            override fun createFromParcel(source: Parcel): YearInfo = YearInfo(source)
            override fun newArray(size: Int): Array<YearInfo?> = arrayOfNulls(size)
        }
    }
}