package com.example.eraofband.remote.user.patchUser

import com.google.gson.annotations.SerializedName

data class PatchUserResponse (
    @SerializedName(value = "code") var code : Int,
    @SerializedName(value = "isSuccess") var isSuccess : Boolean,
    @SerializedName(value = "message") var message : String,
    @SerializedName(value = "result") var result : String
)