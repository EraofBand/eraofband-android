package com.example.eraofband.remote.patchuser

import com.example.eraofband.data.EditUser
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

interface PatchUserInterface {
    @PATCH("/users/user-info")
    fun patchUser(@Header("X-ACCESS-TOKEN") jwt: String,
                  @Body user: EditUser
    ): Call<PatchUserResponse>
}

