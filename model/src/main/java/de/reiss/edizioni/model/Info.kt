package de.reiss.edizioni.model

import android.os.Parcel
import android.os.Parcelable

data class Info(val infoBlocks: List<InfoBlock>) : Parcelable {
    constructor(source: Parcel) : this(
            source.createTypedArrayList(InfoBlock.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(infoBlocks)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Info> = object : Parcelable.Creator<Info> {
            override fun createFromParcel(source: Parcel): Info = Info(source)
            override fun newArray(size: Int): Array<Info?> = arrayOfNulls(size)
        }
    }
}