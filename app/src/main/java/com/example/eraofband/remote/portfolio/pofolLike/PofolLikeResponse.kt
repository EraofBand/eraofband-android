package com.example.eraofband.remote.portfolio.pofolLike

import com.google.gson.annotations.SerializedName

data class PofolLikeResponse (
    @SerializedName(value = "code") var code : Int,
    @SerializedName(value = "isSuccess") var isSuccess : Boolean,
    @SerializedName(value = "message") var message : String,
    @SerializedName(value = "result") var result : PofolLikeResult
)

data class PofolLikeResult (
    @SerializedName(value = "pofolLikeIdx") var pofolLikeIdx : Int
)