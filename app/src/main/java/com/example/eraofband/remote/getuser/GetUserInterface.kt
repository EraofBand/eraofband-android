package com.example.eraofband.remote.getuser

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetUserInterface {
    @GET("/users/{userIdx}")
    fun getUser(@Path("userIdx") userIdx : Int) : Call<GetUserResponse>

}