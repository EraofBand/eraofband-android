package com.example.eraofband.remote.getMyPofol

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetMyPofolInterface {
    @GET("/pofol/my/")
    fun getUser(@Query("userIdx") userIdx: Int) : Call<GetMyPofolResponse>
}