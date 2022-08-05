package com.example.eraofband.remote.search.getUser


data class GetSearchUserResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: List<GetSearchUserResult>
)

data class GetSearchUserResult(
    var nickName : String,
    var profileImgUrl : String,
    var userIdx : Int,
    var userSession : Int
    )