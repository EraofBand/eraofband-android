package com.example.eraofband.remote.patchuser

import com.google.gson.annotations.SerializedName

data class PatchUserResponse (
    @SerializedName(value = "code") var code : Int,
    @SerializedName(value = "isSuccess") var isSuccess : Boolean,
    @SerializedName(value = "message") var message : String,
    @SerializedName(value = "result") var result : PatchUserResult
)

data class PatchUserResult (
    @SerializedName(value = "birth") var birth : String,
    @SerializedName(value = "gender") var gender : String,
    @SerializedName(value = "introduction") var introduction : String,
    @SerializedName(value = "nickName") var nickName : String,
    @SerializedName(value = "profileImgUrl") var profileImgUrl : String,
    @SerializedName(value = "region") var region : String,
    @SerializedName(value = "userIdx") var userIdx : Int
)