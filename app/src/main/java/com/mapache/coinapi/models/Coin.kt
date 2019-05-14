package com.mapache.coinapi.models

import android.os.Parcel
import android.os.Parcelable

data class Coin (
    var _id:String,
    var name: String,
    var country: String,
    var value: Int,
    var values_us: Double,
    var year: Int,
    var review: String,
    var isAvailable: Boolean,
    var img: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        _id = parcel.readString(),
        name = parcel.readString(),
        country = parcel.readString(),
        value = parcel.readInt(),
        values_us = parcel.readDouble(),
        year = parcel.readInt(),
        review = parcel.readString(),
        isAvailable = parcel.readInt() == 1,
        img = parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(name)
        parcel.writeString(country)
        parcel.writeInt(value)
        parcel.writeDouble(values_us)
        parcel.writeInt(year)
        parcel.writeString(review)
        if(isAvailable) parcel.writeInt(1)
        else parcel.writeInt(0)
        parcel.writeString(img)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Coin> {
        override fun createFromParcel(parcel: Parcel): Coin {
            return Coin(parcel)
        }

        override fun newArray(size: Int): Array<Coin?> {
            return arrayOfNulls(size)
        }
    }
}