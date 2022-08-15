package com.example.eraofband.remote.portfolio.getPofol

data class GetPofolResponse (
    var code : Int,
    var isSuccess : Boolean,
    var message : String,
    var result : List<GetPofolResult>
)

data class GetPofolResult (
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