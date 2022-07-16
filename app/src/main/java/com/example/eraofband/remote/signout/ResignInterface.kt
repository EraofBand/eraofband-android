package com.example.eraofband.remote.signout

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ResignInterface {
    @PATCH("/users/{userIdx}/delete")
    fun resign(@Header("X-ACCESS-TOKEN") jwt: String, @Path("userIdx") userIdx: Int) : Call<ResignResponse>
}