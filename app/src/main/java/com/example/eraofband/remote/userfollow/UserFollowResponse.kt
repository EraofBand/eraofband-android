package com.example.eraofband.remote.userfollow

import com.google.gson.annotations.SerializedName

data class UserFollowResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UserFollowResult
)

data class UserFollowResult (
    var followIdx : Int
)