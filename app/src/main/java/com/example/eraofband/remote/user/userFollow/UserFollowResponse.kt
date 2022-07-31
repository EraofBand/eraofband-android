package com.example.eraofband.remote.user.userFollow

data class UserFollowResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UserFollowResult
)

data class UserFollowResult (
    var followIdx : Int
)