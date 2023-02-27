package com.geektech.pixa51

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PixaApi {
    @GET("api/")
    fun getImages(
        @Query("key") key: String = "33905755-32f67e30b3dd2407f2f0ce340",
        @Query("q") keyWord: String,
        @Query("per_page") perPage: Int = 3,
        @Query("page") page: Int
    ): Call<PixaModel>
}
