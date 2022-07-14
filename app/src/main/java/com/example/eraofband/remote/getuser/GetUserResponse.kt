package com.example.eraofband.remote.getuser

import com.google.gson.annotations.SerializedName

data class GetUserResponse (
    @SerializedName(value = "code") var code : Int,
    @SerializedName(value = "isSuccess") var isSuccess : Boolean,
    @SerializedName(value = "message") var message : String,
    @SerializedName(value = "result") var result : GetUserResult
)

data class GetUserResult (
    @SerializedName(value = "birth") var birth : String,
    @SerializedName(value = "followeeCount") var followeeCount : Int,
    @SerializedName(value = "followerCount") var followerCount : Int,
    @SerializedName(value = "gender") var gender : String,
    @SerializedName(value = "instroduction") var instroduction : String,
    @SerializedName(value = "nickName") var nickName : String,
    @SerializedName(value = "pofolCount") var pofolCount : Int,
    @SerializedName(value = "profileImgUrl") var profileImgUrl : String,
    @SerializedName(value = "region") var region : String,
    @SerializedName(value = "session") var session : Int,
    @SerializedName(value = "userIdx") var userIdx : Int
)