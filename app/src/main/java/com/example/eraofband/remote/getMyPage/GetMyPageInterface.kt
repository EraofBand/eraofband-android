package com.example.eraofband.remote.getMyPage

import com.example.eraofband.remote.getMyPage.GetMyPageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GetMyPageInterface {
    @GET("/users/myPage/{userIdx}")
    fun getUser(@Header("X-ACCESS-TOKEN") jwt : String, @Path("userIdx") userIdx : Int) : Call<GetMyPageResponse>

}