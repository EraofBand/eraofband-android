package com.example.eraofband.remote.user.checkUser

data class CheckUserResponse(
    var code : Int,
    var isSuccess : String,
    var message : String,
    var result : CheckUserResult
)

data class CheckUserResult(
    var expiration: Long,
    var jwt: String,
    var refresh: String,
    var userIdx: Int
)