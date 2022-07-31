package com.example.eraofband.remote.user.getMyPage

data class GetMyPageResponse (
    var code : Int,
    var isSuccess : Boolean,
    var message : String,
    var result : GetMyPageResult
)

data class GetMyPageResult (
    var getUser: GetUser,
    var getUserBand: List<GetUserBand>,
    var getUserLesson: List<GetUserLesson>,
    var getUserPofol: List<GetUserPofol>
)

data class GetUser (
    var birth : String,
    var followeeCount : Int,
    var followerCount : Int,
    var gender : String,
    var introduction : String,
    var nickName : String,
    var pofolCount : Int,
    var profileImgUrl : String,
    var region : String,
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

data class GetUserLesson (
    var capacity : Int,
    var lessonIdx : Int,
    var lessonImgUrl : String,
    var lessonIntroduction : String,
    var lessonRegion : String,
    var lessonTitle : String,
    var memberCount : Int
)

data class GetUserPofol (
    var imgUrl : String,
    var pofolIdx : Int
)