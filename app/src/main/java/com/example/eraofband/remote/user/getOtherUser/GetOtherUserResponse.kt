package com.example.eraofband.remote.user.getOtherUser

data class GetOtherUserResponse (
    var code : Int,
    var isSuccess : Boolean,
    var message : String,
    var result : GetOtherUserResult
)

data class GetOtherUserResult (
    var getUser: GetUser,
    var getUserBand: List<GetUserBand>,
    var getUserPofol: List<GetUserPofol>
)

data class GetUser (
    var birth : String,
    var blockStatus: Int,
    var follow : Int,
    var followeeCount : Int,
    var followerCount : Int,
    var gender : String,
    var introduction : String,
    var nickName : String,
    var pofolCount : Int,
    var profileImgUrl : String,
    var region : String,
    var userIdx : Int,
    var userSession : Int
)

data class GetUserBand (
    var bandIdx : Int,
    var bandImgUrl : String,
    var bandIntroduction : String,
    var bandRegion : String,
    var bandTitle : String,
    var capacity : Int,
    var memberCount : Int
)

data class GetUserPofol (
    var imgUrl : String,
    var pofolIdx : Int
)