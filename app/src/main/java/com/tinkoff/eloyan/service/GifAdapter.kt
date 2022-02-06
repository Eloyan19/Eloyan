package com.tinkoff.eloyan.service


import com.tinkoff.eloyan.data.GifItem
import com.tinkoff.eloyan.data.GifResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GifAdapter @Inject constructor(private val gifService: GifService) {

    suspend fun getRandomGif(): GifItem {
        return gifService.getRandomGif()
    }

    suspend fun getLatestGifs(): GifResponse {
        return gifService.getLatestGifs(0)
    }

}