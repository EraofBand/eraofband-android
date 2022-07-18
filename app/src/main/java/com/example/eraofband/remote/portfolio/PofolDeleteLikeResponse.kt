package com.example.eraofband.remote.portfolio

import com.google.gson.annotations.SerializedName

data class PofolDeleteLikeResponse (
    @SerializedName(value = "code") var code : Int,
    @SerializedName(value = "isSuccess") var isSuccess : Boolean,
    @SerializedName(value = "message") var message : String,
    @SerializedName(value = "result") var result : String
)