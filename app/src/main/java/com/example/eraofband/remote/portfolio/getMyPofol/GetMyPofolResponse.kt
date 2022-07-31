package com.example.eraofband.remote.portfolio.getMyPofol

data class GetMyPofolResponse (
    var code : Int,
    var isSuccess : Boolean,
    var message : String,
    var result : List<GetMyPofolResult>
)

data class GetMyPofolResult (
    var commentCount : Int,
    var content : String,
    var likeOrNot : String,
    var nickName : String,
    var pofolIdx : Int,
    var pofolLikeCount : Int,
    var profileImgUrl : String,
    var title : String,
    var updatedAt : String,
    var userIdx : Int,
    var videoUrl : String
)