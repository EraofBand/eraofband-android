package com.example.eraofband.remote.patchuser

import com.example.eraofband.data.User
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

interface PatchUserInterface {
    @FormUrlEncoded
    @PATCH("/users/modiUserInfo")
    fun patchUser(@Header("X-ACCESS-TOKEN") jwt : String,
                  @Body user : User): Call<PatchUserResponse>
}

