package de.reiss.edizioni.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class DailyText(val date: Date,
                     val verse: String,
                     val bibleRef: String,
                     val devotions: List<String>,
                     val author: String) : Comparable<DailyText>, Parcelable {

    override fun compareTo(other: DailyText): Int = this.date.compareTo(other.date)

    constructor(source: Parcel) : this(
            source.readSerializable() as Date,
            source.readString(),
            source.readString(),
            source.createStringArrayList(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeSerializable(date)
        writeString(verse)
        writeString(bibleRef)
        writeStringList(devotions)
        writeString(author)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DailyText> = object : Parcelable.Creator<DailyText> {
            override fun createFromParcel(source: Parcel): DailyText = DailyText(source)
            override fun newArray(size: Int): Array<DailyText?> = arrayOfNulls(size)
        }
    }
}