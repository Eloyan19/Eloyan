package com.tinkoff.eloyan.service

import com.tinkoff.eloyan.data.GifItem
import com.tinkoff.eloyan.data.GifResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GifService {
    companion object {
        const val BASE_URL = "https://developerslife.ru/"
    }

    @GET("random")
    suspend fun getRandomGif(@Query("json") json: Boolean = true): GifItem

    @GET("latest/{page}")
    suspend fun getLatestGifs(
        @Path("page") page: Int,
        @Query("json") json: Boolean = true
    ): GifResponse
}