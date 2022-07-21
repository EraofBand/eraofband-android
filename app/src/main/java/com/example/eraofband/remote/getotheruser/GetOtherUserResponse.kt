package com.example.eraofband.remote.getotheruser

import com.google.gson.annotations.SerializedName

data class GetOtherUserResponse (
    @SerializedName(value = "code") var code : Int,
    @SerializedName(value = "isSuccess") var isSuccess : Boolean,
    @SerializedName(value = "message") var message : String,
    @SerializedName(value = "result") var result : GetOtherUserResult
)

data class GetOtherUserResult (
    @SerializedName(value = "getUser") var getUser: GetUser,
    @SerializedName(value = "getUserBand") var getUserBand: List<GetUserBand>,
    @SerializedName(value = "getUserPofol") var getUserPofol: List<GetUserPofol>
)

data class GetUser (
    @SerializedName(value = "birth") var birth : String,
    @SerializedName(value = "follow") var follow : Int,
    @SerializedName(value = "followeeCount") var followeeCount : Int,
    @SerializedName(value = "followerCount") var followerCount : Int,
    @SerializedName(value = "gender") var gender : String,
    @SerializedName(value = "introduction") var introduction : String,
    @SerializedName(value = "nickName") var nickName : String,
    @SerializedName(value = "pofolCount") var pofolCount : Int,
    @SerializedName(value = "profileImgUrl") var profileImgUrl : String,
    @SerializedName(value = "region") var region : String,
    @SerializedName(value = "userSession") var mySession : Int,
    @SerializedName(value = "userIdx") var userIdx : Int
)

data class GetUserBand (
    @SerializedName(value = "bandIdx") var bandIdx : Int,
    @SerializedName(value = "bandImgUrl") var bandImgUrl : String,
    @SerializedName(value = "bandIntroduction") var bandIntroduction : String,
    @SerializedName(value = "bandRegion") var bandRegion : String,
    @SerializedName(value = "bandTitle") var bandTitle : String,
    @SerializedName(value = "capacity") var capacity : Int,
    @SerializedName(value = "memberCount") var memberCount : Int
)

data class GetUserPofol (
    @SerializedName(value = "imgUrl") var imgUrl : String,
    @SerializedName(value = "pofolIdx") var pofolIdx : Int
)