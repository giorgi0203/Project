package com.example.project

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by realtynaNick on 19.02.18.
 */

class RFile(var id: Int = 0, var url: String? = null, var filename: String? = null, var name: String? = null, var type: String? = null, var caption: String? = null, var isYoutube: Boolean = false, var filePath: String? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        false,
        parcel.readString())


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(url)
        parcel.writeString(filename)
        parcel.writeString(name)
        parcel.writeString(type)
        parcel.writeString(caption)
        parcel.writeByte((if (isYoutube) 1 else 0).toByte())
        parcel.writeString(filePath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RFile> {
        override fun createFromParcel(parcel: Parcel): RFile {
            return RFile(parcel)
        }

        override fun newArray(size: Int): Array<RFile?> {
            return arrayOfNulls(size)
        }
    }
}