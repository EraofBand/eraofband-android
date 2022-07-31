package com.example.eraofband.remote.user.patchSession

import com.google.gson.annotations.SerializedName

data class PatchSessionResponse(
    @SerializedName(value = "code") var code : Int,
    @SerializedName(value = "isSuccess") var isSuccess : Boolean,
    @SerializedName(value = "message") var message : String,
    @SerializedName(value = "result") var result : String
)
