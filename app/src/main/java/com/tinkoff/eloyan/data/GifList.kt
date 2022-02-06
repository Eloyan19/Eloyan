package com.tinkoff.eloyan.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GifList (
    val gifList: ArrayList<GifItem>,
    var currentPosition: Int
): Parcelable