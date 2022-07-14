package com.example.eraofband.remote.checkUser

import retrofit2.Call
import retrofit2.http.PATCH
import retrofit2.http.Path

interface CheckUserInterface {
    @PATCH("/users/login/{kakao-email}")
    fun checkUser(@Path("kakao-email") email : String) : Call<CheckUserResponse>
}