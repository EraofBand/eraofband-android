package com.example.eraofband.remote.checkUser

import retrofit2.Call
import retrofit2.http.PATCH
import retrofit2.http.Path

interface CheckUserInterface {
    @PATCH("/users/login/{kakao-email}")
    fun CheckUser(@Path("kakao-email") email : String) : Call<CheckUserResponse>
}