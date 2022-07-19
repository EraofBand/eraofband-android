package com.example.eraofband.remote.getotheruser

import com.example.eraofband.remote.getotheruser.GetOtherUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GetOtherUserInterface {
    @GET("/users/{userIdx}")
    fun getUser(@Header("X-ACCESS-TOKEN") jwt : String, @Path("userIdx") userIdx : Int) : Call<GetOtherUserResponse>

}