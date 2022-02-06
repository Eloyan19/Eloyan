package com.tinkoff.eloyan.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GifResponse(
    val totalCount: Int,
    val result: List<GifItem>
): Parcelable